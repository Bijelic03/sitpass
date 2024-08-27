package com.ftn.sitpass.controller;

import com.ftn.sitpass.dto.ExerciseDto;
import com.ftn.sitpass.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/facilities/{facilityId}/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ExerciseDto>> getAllFacilityExercises(@PathVariable Long facilityId) {
        return  ResponseEntity.ok(exerciseService.getAllByFacilityId(facilityId));
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<ExerciseDto> createReservation(@PathVariable Long facilityId,
                                                         @RequestBody ExerciseDto exerciseDto) {
        return ResponseEntity.ok(exerciseService.createReservation(facilityId, exerciseDto));
    }

}
