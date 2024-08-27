package com.ftn.sitpass.controller;

import com.ftn.sitpass.dto.ReviewDto;
import com.ftn.sitpass.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/facilities/{facilityId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ReviewDto>> getAllReviews(@PathVariable Long facilityId) {
        return ResponseEntity.ok(reviewService.getReviewsByFacilityId(facilityId));
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<ReviewDto> createReview(@PathVariable Long facilityId, @RequestBody ReviewDto reviewDto) {
        return ResponseEntity.ok(reviewService.create(facilityId, reviewDto));
    }

}
