package com.ftn.sitpass.service.impl;

import com.ftn.sitpass.dto.DisciplineDto;
import com.ftn.sitpass.dto.FacilityDto;
import com.ftn.sitpass.dto.WorkDayDto;
import com.ftn.sitpass.exception.BadRequestException;
import com.ftn.sitpass.exception.NotFoundException;
import com.ftn.sitpass.model.Discipline;
import com.ftn.sitpass.model.Facility;
import com.ftn.sitpass.model.WorkDay;
import com.ftn.sitpass.repository.DisciplineRepository;
import com.ftn.sitpass.repository.FacilityRepository;
import com.ftn.sitpass.repository.WorkDayRepository;
import com.ftn.sitpass.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;

    private final DisciplineRepository disciplineRepository;

    private final WorkDayRepository workDayRepository;

    @Override
    public Facility getModel(Long id) {
        return facilityRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Facility with id: %s not found!", id)));
    }

    @Override
    public List<FacilityDto> getAll() {
        return facilityRepository.findAll()
                .stream()
                .map(FacilityDto::convertToDto)
                .toList();
    }

    @Override
    public FacilityDto get(Long id) {
        return FacilityDto.convertToDto(facilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Facility with id: %s not found!", id))));

    }

    @Override
    public void create(FacilityDto facilityDto) {
        Facility facility = facilityRepository.save(facilityDto.convertToModel());
        facilityDto.getDisciplines()
                .forEach(disciplineDto -> {
                    saveDiscipline(disciplineDto, facility);
                });

        facilityDto.getWorkDays()
                .forEach(workDayDto -> {
                    saveWorkDay(workDayDto, facility);
                });

    }

    @Override
    public void update(Long id, FacilityDto facilityDto) {
        Facility facility = getModel(id);

        facilityDto.getDisciplines()
                .forEach(disciplineDto -> {
                    if(disciplineDto.getId() == null) {
                        saveDiscipline(disciplineDto, facility);
                    } else {
                        Discipline discipline = disciplineRepository.getReferenceById(disciplineDto.getId());
                        discipline.setName(disciplineDto.getName());
                        disciplineRepository.save(discipline);
                    }
                });

        facilityDto.getWorkDays()
                .forEach(workDayDto -> {
                    if(workDayDto.getId() == null) {
                        saveWorkDay(workDayDto, facility);
                    } else {
                        WorkDay workDay = workDayRepository.getReferenceById(workDayDto.getId());
                        workDay.setFromTime(workDayDto.getFromTime());
                        workDay.setUntilTime(workDayDto.getUntilTime());
                        workDay.setValidFrom(workDayDto.getValidFrom());
                        workDayRepository.save(workDay);
                    }
                });

        facility.setCity(facilityDto.getCity());
        facility.setActive(facilityDto.isActive());
        facility.setAddress(facilityDto.getAddress());
        facility.setDescription(facilityDto.getDescription());
        facility.setName(facilityDto.getName());

        facilityRepository.save(facility);

    }

    @Override
    public void updateTotalRating(Long id, Double newTotalRating) {
        Facility facility = getModel(id);
        facility.setTotalRating(newTotalRating);

        facilityRepository.save(facility);
    }

    @Override
    public void delete(Long id) {
        facilityRepository.delete(getModel(id));
    }

    @Override
    public WorkDay getValidWorkday(Long facilityId, LocalDateTime date) {
        return getModel(facilityId).getWorkDays().stream()
                .filter(workDay -> workDay.getDay() == date.getDayOfWeek()
                        && workDay.getValidFrom().isBefore(ChronoLocalDate.from(date)))
                .max(Comparator.comparing(WorkDay::getValidFrom))
                .orElseThrow(() -> new BadRequestException(String.format("Facility is not working at: %s",
                        date.getDayOfWeek())));
    }

    private void saveDiscipline(DisciplineDto disciplineDto, Facility facility) {
        Discipline discipline = disciplineDto.convertToModel();
        discipline.setFacility(facility);
        disciplineRepository.save(discipline);
    }

    private void saveWorkDay(WorkDayDto workDayDto, Facility facility) {
        WorkDay workDay = workDayDto.convertToModel();
        workDay.setFacility(facility);
        workDayRepository.save(workDay);
    }

}
