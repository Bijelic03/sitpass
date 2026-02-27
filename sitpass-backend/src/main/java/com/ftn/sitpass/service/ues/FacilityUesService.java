package com.ftn.sitpass.service.ues;

import com.ftn.sitpass.dto.ues.FacilitySearchHitDto;
import com.ftn.sitpass.dto.ues.FacilitySearchRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FacilityUesService {
    void reindexAllFacilities();
    void reindexFacility(Long facilityId);
    void deleteFromIndex(Long facilityId);
    void uploadAssets(Long facilityId, List<MultipartFile> images, MultipartFile document);
    byte[] downloadImage(Long imageId);
    byte[] downloadFacilityDocument(Long facilityId);
    String getDocumentFileName(Long facilityId);
    String getDocumentContentType(Long facilityId);
    List<FacilitySearchHitDto> search(FacilitySearchRequest request);
}
