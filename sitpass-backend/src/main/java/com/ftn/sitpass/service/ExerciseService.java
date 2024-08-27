package com.ftn.sitpass.service;

import com.ftn.sitpass.dto.ExerciseDto;

import java.util.List;

public interface ExerciseService {
    List<ExerciseDto> getAllByFacilityId(Long facilityId);

    ExerciseDto createReservation(Long facilityId, ExerciseDto exerciseDto);

}
