package com.ftn.sitpass.service.impl;

import com.ftn.sitpass.dto.ReviewDto;
import com.ftn.sitpass.model.Comment;
import com.ftn.sitpass.model.Rate;
import com.ftn.sitpass.model.Review;
import com.ftn.sitpass.model.User;
import com.ftn.sitpass.repository.RateRepository;
import com.ftn.sitpass.repository.ReviewRepository;
import com.ftn.sitpass.service.FacilityService;
import com.ftn.sitpass.service.ReviewService;
import com.ftn.sitpass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserService userService;

    private final FacilityService facilityService;

    private final RateRepository rateRepository;

    @Override
    public List<ReviewDto> getReviewsByFacilityId(Long id) {
        return reviewRepository.findAllByFacilityId(id)
                .stream()
                .map(ReviewDto::convertToDto)
                .toList();
    }

    @Override
    public Double getAverageFacilityRating(Long id) {
        return reviewRepository.findAllByFacilityId(id).stream().map(Review::getRating)
                .mapToDouble(Rate::getAverage)
                .average()
                .orElse(0.0);
    }

    @Override
    public ReviewDto create(Long facilityId, ReviewDto reviewDto) {
        User author = userService.getUserModel(reviewDto.getAuthorId());

        Review review = reviewDto.convertToModel();
        Rate rating = Rate.builder()
                .staff(reviewDto.getStaffRate())
                .equipment(reviewDto.getEquipmentRate())
                .hygiene(reviewDto.getHygieneRate())
                .space(reviewDto.getSpaceRate())
                .build();


//        Comment comment = reviewDto.getCommentDto().convertToModel();
//        comment.setAuthor(author);

        review.setAuthor(author);
        review.setFacility(facilityService.getModel(facilityId));
//        review.setComment(comment);
        review.setRating(rateRepository.save(rating));

        ReviewDto reviewDto1 = ReviewDto.convertToDto(reviewRepository.save(review));
        facilityService.updateTotalRating(facilityId, getAverageFacilityRating(facilityId));

        return reviewDto1;
    }


}
