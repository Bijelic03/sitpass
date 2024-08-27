package com.ftn.sitpass.dto;

import com.ftn.sitpass.model.Exercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto {

    private Long id;

    private Long userId;

    private Long facilityId;

    private LocalDateTime fromTime;

    private LocalDateTime untilTime;

    public static ExerciseDto convertToDto(Exercise exercise) {
        return ExerciseDto.builder()
                .id(exercise.getId())
                .userId(exercise.getUser().getId())
                .facilityId(exercise.getFacility().getId())
                .fromTime(exercise.getFromTime())
                .untilTime(exercise.getUntilTime())
                .build();
    }

    public Exercise convertToModel() {
        return Exercise.builder()
                .id(getId())
                .fromTime(getFromTime())
                .untilTime(getUntilTime())
                .build();
    }
}
