package com.ftn.sitpass.dto;


import com.ftn.sitpass.model.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;

    private CommentDto commentDto;

    private Long authorId;

    private Long facilityId;

    private String authorName;

    private LocalDateTime createdAt;

    private Integer exerciseCount;

    private Boolean hidden;

    private Integer staffRate;

    private Integer equipmentRate;

    private Integer hygieneRate;

    private Integer spaceRate;

    public static ReviewDto convertToDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
//                .commentDto(CommentDto.convertToDto(review.getComment()))
                .authorId(review.getAuthor().getId())
                .facilityId(review.getFacility().getId())
                .authorName(review.getAuthor().getName() + " " + review.getAuthor().getSurname())
                .createdAt(review.getCreatedAt())
                .exerciseCount(review.getExerciseCount())
                .hidden(review.getHidden())
                .staffRate(review.getRating().getStaff())
                .equipmentRate(review.getRating().getEquipment())
                .hygieneRate(review.getRating().getHygiene())
                .spaceRate(review.getRating().getSpace())
                .build();
    }

    public Review convertToModel() {
        return Review.builder()
                .id(getId())
                .createdAt(getCreatedAt())
                .exerciseCount(getExerciseCount())
                .hidden(getHidden())
                .build();
    }

}
