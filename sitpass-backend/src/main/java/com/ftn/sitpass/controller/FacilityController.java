package com.ftn.sitpass.controller;

import com.ftn.sitpass.dto.FacilityDto;
import com.ftn.sitpass.dto.ues.FacilitySearchHitDto;
import com.ftn.sitpass.dto.ues.FacilitySearchRequest;
import com.ftn.sitpass.service.FacilityService;
import com.ftn.sitpass.service.ues.FacilityUesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/facilities")
public class FacilityController {

    private final FacilityService facilityService;
    private final FacilityUesService facilityUesService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<FacilityDto>> getFacilities() {
        return  ResponseEntity.ok(facilityService.getAll());
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FacilityDto> getFacility(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.get(id));
    }

    @PostMapping()
    public ResponseEntity<Void> createFacility(@RequestBody FacilityDto facilityDto) {
        facilityService.create(facilityDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFacility(@PathVariable Long id, @RequestBody FacilityDto facilityDto) {
       facilityService.update(id, facilityDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
        facilityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{id}/assets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFacilityAssets(@PathVariable Long id,
                                                     @RequestPart(required = false) List<MultipartFile> images,
                                                     @RequestPart(required = false) MultipartFile document) {
        facilityUesService.uploadAssets(id, images, document);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/images/{imageId}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable Long imageId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(facilityUesService.downloadImage(imageId));
    }

    @GetMapping(path = "/{id}/document")
    public ResponseEntity<byte[]> downloadFacilityDocument(@PathVariable Long id) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + facilityUesService.getDocumentFileName(id) + "\"")
                .contentType(MediaType.parseMediaType(facilityUesService.getDocumentContentType(id)))
                .body(facilityUesService.downloadFacilityDocument(id));
    }

    @PostMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FacilitySearchHitDto>> searchFacilities(@RequestBody FacilitySearchRequest request) {
        return ResponseEntity.ok(facilityUesService.search(request));
    }
}
