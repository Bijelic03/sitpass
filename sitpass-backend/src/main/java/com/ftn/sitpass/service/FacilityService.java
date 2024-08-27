package com.ftn.sitpass.service;

import com.ftn.sitpass.dto.FacilityDto;
import com.ftn.sitpass.model.Facility;
import com.ftn.sitpass.model.Rate;
import com.ftn.sitpass.model.WorkDay;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public interface FacilityService {

    Facility getModel(Long id);

    List<FacilityDto> getAll();

    FacilityDto get(Long id);

    void create(FacilityDto facilityDto);

    void update(Long id, FacilityDto facilityDto);

    void updateTotalRating(Long id, Double newTotalRating);

    void delete(Long id);

    WorkDay getValidWorkday(Long facilityId, LocalDateTime date);
}
