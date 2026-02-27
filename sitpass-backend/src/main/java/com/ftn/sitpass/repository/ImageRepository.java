package com.ftn.sitpass.repository;

import com.ftn.sitpass.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByFacilityId(Long facilityId);
}
