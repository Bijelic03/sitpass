package com.ftn.sitpass.service;

import com.ftn.sitpass.dto.ReviewDto;
import com.ftn.sitpass.model.Review;

import java.util.List;

public interface ReviewService {
    List<ReviewDto> getReviewsByFacilityId(Long id);

    Double getAverageFacilityRating(Long id);

    ReviewDto create(Long facilityId, ReviewDto reviewDto);

}
