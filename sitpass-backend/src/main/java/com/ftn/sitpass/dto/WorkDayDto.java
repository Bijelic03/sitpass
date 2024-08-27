package com.ftn.sitpass.dto;

import com.ftn.sitpass.model.WorkDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkDayDto {

    private Long id;

    private Long facilityId;

    private LocalDate validFrom;

    private String day;

    private LocalTime fromTime;

    private LocalTime untilTime;

    public static WorkDayDto convertToDto(WorkDay workDay) {
        return WorkDayDto.builder()
                .id(workDay.getId())
                .validFrom(workDay.getValidFrom())
                .day(workDay.getDay().toString())
                .fromTime(workDay.getFromTime())
                .untilTime(workDay.getUntilTime())
                .facilityId(workDay.getFacility().getId())
                .build();
    }

    public WorkDay convertToModel() {
        return WorkDay.builder()
                .id(getId())
                .validFrom(getValidFrom())
                .day(DayOfWeek.valueOf(getDay()))
                .fromTime(getFromTime())
                .untilTime(getUntilTime())
                .build();
    }

}