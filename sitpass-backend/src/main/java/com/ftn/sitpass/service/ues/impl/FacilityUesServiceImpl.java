package com.ftn.sitpass.service.ues.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ftn.sitpass.dto.ues.FacilitySearchHitDto;
import com.ftn.sitpass.dto.ues.FacilitySearchRequest;
import com.ftn.sitpass.exception.BadRequestException;
import com.ftn.sitpass.exception.NotFoundException;
import com.ftn.sitpass.model.Discipline;
import com.ftn.sitpass.model.Facility;
import com.ftn.sitpass.model.FacilityDocument;
import com.ftn.sitpass.model.Image;
import com.ftn.sitpass.repository.FacilityDocumentRepository;
import com.ftn.sitpass.repository.FacilityRepository;
import com.ftn.sitpass.repository.ImageRepository;
import com.ftn.sitpass.repository.ReviewRepository;
import com.ftn.sitpass.service.storage.ObjectStorageService;
import com.ftn.sitpass.service.ues.FacilityUesService;
import com.ftn.sitpass.service.ues.PdfTextExtractor;
import com.ftn.sitpass.ues.FacilitySearchDocument;
import com.ftn.sitpass.ues.FacilitySearchRepository;
import com.ibm.icu.text.Transliterator;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacilityUesServiceImpl implements FacilityUesService {

    private static final String IMAGE_PATH_PREFIX = "/api/v1/facilities/images/";
    private static final Transliterator TRANSLITERATOR =
            Transliterator.getInstance("Any-Latin; NFD; [:Nonspacing Mark:] Remove; NFC");

    private final FacilityRepository facilityRepository;
    private final ImageRepository imageRepository;
    private final FacilityDocumentRepository facilityDocumentRepository;
    private final ReviewRepository reviewRepository;
    private final FacilitySearchRepository facilitySearchRepository;
    private final ObjectStorageService objectStorageService;
    private final PdfTextExtractor pdfTextExtractor;
    private final ElasticsearchClient elasticsearchClient;

    @Override
    @Transactional(readOnly = true)
    public void reindexAllFacilities() {
        facilityRepository.findAll().forEach(facility -> reindexFacility(facility.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public void reindexFacility(Long facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NotFoundException("Facility not found for indexing."));
        FacilityDocument facilityDocument = facilityDocumentRepository.findByFacilityId(facilityId).orElse(null);

        FacilitySearchDocument searchDocument = FacilitySearchDocument.builder()
                .id(facility.getId())
                .name(facility.getName())
                .nameSort(normalize(facility.getName()))
                .nameNormalized(normalize(facility.getName()))
                .description(facility.getDescription())
                .descriptionNormalized(normalize(facility.getDescription()))
                .pdfDescription(facilityDocument == null ? "" : nullSafe(facilityDocument.getExtractedText()))
                .pdfDescriptionNormalized(facilityDocument == null ? "" : normalize(facilityDocument.getExtractedText()))
                .city(facility.getCity())
                .disciplines(facility.getDisciplines() == null
                        ? Collections.emptyList()
                        : facility.getDisciplines().stream().map(Discipline::getName).toList())
                .reviewCount((int) reviewRepository.countByFacilityId(facilityId))
                .avgStaff(nullSafe(reviewRepository.averageStaffByFacilityId(facilityId)))
                .avgEquipment(nullSafe(reviewRepository.averageEquipmentByFacilityId(facilityId)))
                .avgHygiene(nullSafe(reviewRepository.averageHygieneByFacilityId(facilityId)))
                .avgSpace(nullSafe(reviewRepository.averageSpaceByFacilityId(facilityId)))
                .createdAt(facility.getCreatedAt() == null ? null : facility.getCreatedAt().toString())
                .build();

        facilitySearchRepository.save(searchDocument);
    }

    @Override
    public void deleteFromIndex(Long facilityId) {
        facilitySearchRepository.deleteById(facilityId);
    }

    @Override
    @Transactional
    public void uploadAssets(Long facilityId, List<MultipartFile> images, MultipartFile document) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NotFoundException("Facility not found."));

        List<MultipartFile> imageFiles = images == null
                ? Collections.emptyList()
                : images.stream().filter(file -> file != null && !file.isEmpty()).toList();

        int existingImageCount = imageRepository.findAllByFacilityId(facilityId).size();
        int resultingImageCount = existingImageCount + imageFiles.size();
        if (!imageFiles.isEmpty() && resultingImageCount < 2) {
            throw new BadRequestException("If images are uploaded, at least two images are required.");
        }

        for (MultipartFile imageFile : imageFiles) {
            String objectKey = "facilities/" + facilityId + "/images/" + UUID.randomUUID() + "-" + imageFile.getOriginalFilename();
            objectStorageService.upload(objectKey, imageFile);

            Image image = imageRepository.save(Image.builder()
                    .facility(facility)
                    .objectKey(objectKey)
                    .originalFileName(imageFile.getOriginalFilename())
                    .contentType(imageFile.getContentType())
                    .path("")
                    .build());
            image.setPath(IMAGE_PATH_PREFIX + image.getId());
            imageRepository.save(image);
        }

        if (document != null && !document.isEmpty()) {
            String objectKey = "facilities/" + facilityId + "/documents/" + UUID.randomUUID() + "-" + document.getOriginalFilename();
            objectStorageService.upload(objectKey, document);
            String extractedText = pdfTextExtractor.extractText(document);

            FacilityDocument metadata = facilityDocumentRepository.findByFacilityId(facilityId)
                    .orElse(FacilityDocument.builder().facility(facility).build());

            if (metadata.getObjectKey() != null && !metadata.getObjectKey().isBlank()) {
                objectStorageService.remove(metadata.getObjectKey());
            }

            metadata.setObjectKey(objectKey);
            metadata.setOriginalFileName(document.getOriginalFilename());
            metadata.setContentType(document.getContentType());
            metadata.setExtractedText(extractedText);
            facilityDocumentRepository.save(metadata);
        }

        reindexFacility(facilityId);
    }

    @Override
    public byte[] downloadImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found."));
        return objectStorageService.download(image.getObjectKey());
    }

    @Override
    public byte[] downloadFacilityDocument(Long facilityId) {
        FacilityDocument metadata = facilityDocumentRepository.findByFacilityId(facilityId)
                .orElseThrow(() -> new NotFoundException("Facility document not found."));
        return objectStorageService.download(metadata.getObjectKey());
    }

    @Override
    public String getDocumentFileName(Long facilityId) {
        return facilityDocumentRepository.findByFacilityId(facilityId)
                .map(FacilityDocument::getOriginalFileName)
                .orElse("facility-document.pdf");
    }

    @Override
    public String getDocumentContentType(Long facilityId) {
        return facilityDocumentRepository.findByFacilityId(facilityId)
                .map(FacilityDocument::getContentType)
                .filter(value -> !value.isBlank())
                .orElse("application/pdf");
    }

    @Override
    public List<FacilitySearchHitDto> search(FacilitySearchRequest request) {
        try {
            Query query = buildSearchQuery(request);
            SearchResponse<FacilitySearchDocument> response = elasticsearchClient.search(s -> s
                            .index("facilities")
                            .query(query)
                            .highlight(h -> h
                                    .preTags("<em>")
                                    .postTags("</em>")
                                    .fields("description", field -> field)
                                    .fields("pdfDescription", field -> field))
                            .sort(sort -> sort.field(field -> field
                                    .field("nameSort")
                                    .order(Boolean.TRUE.equals(request.getSortByNameAsc()) ? SortOrder.Asc : SortOrder.Desc)))
                            .size(100),
                    FacilitySearchDocument.class);

            List<FacilitySearchHitDto> results = new ArrayList<>();
            for (Hit<FacilitySearchDocument> hit : response.hits().hits()) {
                FacilitySearchDocument source = hit.source();
                if (source == null) {
                    continue;
                }
                results.add(FacilitySearchHitDto.builder()
                        .facilityId(source.getId())
                        .name(source.getName())
                        .description(source.getDescription())
                        .summary(resolveSummary(hit.highlight(), source.getDescription()))
                        .build());
            }
            return results;
        } catch (Exception e) {
            log.error("Elasticsearch search failed. Request: {}", request, e);
            throw new BadRequestException("Unable to perform Elasticsearch search.");
        }
    }

    private Query buildSearchQuery(FacilitySearchRequest request) {
        if (request.getMoreLikeThisFacilityId() != null) {
            FacilitySearchDocument source = facilitySearchRepository.findById(request.getMoreLikeThisFacilityId())
                    .orElseThrow(() -> new NotFoundException("Facility for more-like-this was not found in index."));
            String seed = normalize(String.join(" ",
                    nullSafe(source.getName()),
                    nullSafe(source.getDescription()),
                    nullSafe(source.getPdfDescription())));
            String mltExpression = Arrays.stream(seed.split("\\s+"))
                    .filter(token -> token.length() > 2)
                    .limit(25)
                    .collect(Collectors.joining(" "));
            if (mltExpression.isBlank()) {
                return Query.of(q -> q.matchAll(m -> m));
            }
            return Query.of(q -> q.queryString(qs -> qs
                    .query("nameNormalized:(" + mltExpression + ") OR descriptionNormalized:(" + mltExpression + ") OR pdfDescriptionNormalized:(" + mltExpression + ")")));
        }

        List<String> clauses = new ArrayList<>();
        addTextClause(clauses, "nameNormalized", request.getName());
        addTextClause(clauses, "descriptionNormalized", request.getDescription());
        addTextClause(clauses, "pdfDescriptionNormalized", request.getPdfDescription());
        addRangeClause(clauses, "reviewCount", request.getMinReviews(), request.getMaxReviews());
        addRangeClause(clauses, "avgStaff", request.getMinAvgStaff(), request.getMaxAvgStaff());
        addRangeClause(clauses, "avgEquipment", request.getMinAvgEquipment(), request.getMaxAvgEquipment());
        addRangeClause(clauses, "avgHygiene", request.getMinAvgHygiene(), request.getMaxAvgHygiene());
        addRangeClause(clauses, "avgSpace", request.getMinAvgSpace(), request.getMaxAvgSpace());

        if (clauses.isEmpty()) {
            return Query.of(q -> q.matchAll(m -> m));
        }

        String joiner = "AND";
        if ("OR".equalsIgnoreCase(request.getOperator())) {
            joiner = "OR";
        }
        String expression = String.join(" " + joiner + " ", clauses);
        return Query.of(q -> q.queryString(qs -> qs.query(expression)));
    }

    private void addTextClause(List<String> clauses, String field, String rawInput) {
        if (rawInput == null || rawInput.isBlank()) {
            return;
        }
        String input = rawInput.trim();
        if (input.startsWith("\"") && input.endsWith("\"") && input.length() >= 2) {
            clauses.add(field + ":\"" + escapeQueryString(normalize(input.substring(1, input.length() - 1))) + "\"");
            return;
        }
        if (input.startsWith("~") && input.length() > 1) {
            clauses.add(field + ":" + escapeQueryString(normalize(input.substring(1))) + "~2");
            return;
        }
        if (input.contains("*")) {
            clauses.add(field + ":" + escapeQueryString(normalize(input).replace("*", "")) + "*");
            return;
        }
        clauses.add(field + ":(" + escapeQueryString(normalize(input)) + ")");
    }

    private void addRangeClause(List<String> clauses, String field, Number min, Number max) {
        if (min == null && max == null) {
            return;
        }
        String lower = min == null ? "*" : min.toString();
        String upper = max == null ? "*" : max.toString();
        clauses.add(field + ":[" + lower + " TO " + upper + "]");
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return TRANSLITERATOR.transliterate(value)
                .toLowerCase()
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String resolveSummary(Map<String, List<String>> highlight, String fallback) {
        if (highlight != null) {
            List<String> descriptionHits = highlight.get("description");
            if (descriptionHits != null && !descriptionHits.isEmpty()) {
                return descriptionHits.get(0);
            }
            List<String> pdfHits = highlight.get("pdfDescription");
            if (pdfHits != null && !pdfHits.isEmpty()) {
                return pdfHits.get(0);
            }
        }
        return nullSafe(fallback);
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }

    private Double nullSafe(Double value) {
        return Objects.requireNonNullElse(value, 0.0);
    }

    private String escapeQueryString(String value) {
        return value.replace("\\", "\\\\")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace("=", "\\=")
                .replace("&&", "\\&&")
                .replace("||", "\\||")
                .replace(">", "\\>")
                .replace("<", "\\<")
                .replace("!", "\\!")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("^", "\\^")
                .replace("~", "\\~")
                .replace("?", "\\?")
                .replace(":", "\\:")
                .replace("/", "\\/");
    }
}
