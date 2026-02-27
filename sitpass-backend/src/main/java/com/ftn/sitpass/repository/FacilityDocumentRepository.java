package com.ftn.sitpass.repository;

import com.ftn.sitpass.model.FacilityDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityDocumentRepository extends JpaRepository<FacilityDocument, Long> {
    Optional<FacilityDocument> findByFacilityId(Long facilityId);
}
