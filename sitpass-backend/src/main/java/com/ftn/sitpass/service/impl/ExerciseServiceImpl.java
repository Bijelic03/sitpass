package com.ftn.sitpass.service.impl;

import com.ftn.sitpass.dto.ExerciseDto;
import com.ftn.sitpass.exception.BadRequestException;
import com.ftn.sitpass.model.Exercise;
import com.ftn.sitpass.model.WorkDay;
import com.ftn.sitpass.repository.ExerciseRepository;
import com.ftn.sitpass.service.ExerciseService;
import com.ftn.sitpass.service.FacilityService;
import com.ftn.sitpass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    private final UserService userService;

    private final FacilityService facilityService;

    @Override
    public List<ExerciseDto> getAllByFacilityId(Long facilityId) {
        return exerciseRepository.findAllByFacilityId(facilityId).stream().map(ExerciseDto::convertToDto).toList();
    }

    @Override
    public ExerciseDto createReservation(Long facilityId, ExerciseDto exerciseDto) {
        Exercise exercise = exerciseDto.convertToModel();
        exercise.setUser(userService.getUserModel(exerciseDto.getUserId()));
        exercise.setFacility(facilityService.getModel(facilityId));

        WorkDay workDay = facilityService.getValidWorkday(facilityId, exercise.getFromTime());

        LocalDate exerciseDate = exercise.getFromTime().toLocalDate();
        LocalDateTime workDayFromDateTime = workDay.getFromTime().atDate(exerciseDate);
        LocalDateTime workDayUntilDateTime = workDay.getUntilTime().atDate(exerciseDate);

        if(exercise.getFromTime().isBefore(workDayFromDateTime)) {
            throw new BadRequestException("The facility's working hours haven't started yet.");
        } else if (exercise.getFromTime().isAfter(workDayUntilDateTime)
                && exercise.getUntilTime().isAfter(workDayUntilDateTime)) {
            throw new BadRequestException("The facility's working hours passed.");
        }


        return ExerciseDto.convertToDto(exerciseRepository.save(exercise));
    }

}