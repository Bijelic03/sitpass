package com.ftn.sitpass.repository;

import com.ftn.sitpass.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByFacilityId(Long facilityId);

}
