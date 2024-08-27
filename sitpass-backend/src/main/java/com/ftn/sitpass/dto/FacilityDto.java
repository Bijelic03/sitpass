package com.ftn.sitpass.dto;

import com.ftn.sitpass.model.Facility;
import com.ftn.sitpass.model.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDto {

    private Long id;

    private String name;

    private String description;

    private LocalDate createdAt;

    private String address;

    private String city;

    private Double totalRating;

    private boolean active;

    private List<String> images;

    private List<WorkDayDto> workDays;

    private List<DisciplineDto> disciplines;


    public static FacilityDto convertToDto(Facility facility) {
        return FacilityDto.builder()
                .id(facility.getId())
                .name(facility.getName())
                .description(facility.getDescription())
                .createdAt(facility.getCreatedAt())
                .address(facility.getAddress())
                .city(facility.getCity())
                .totalRating(facility.getTotalRating())
                .active(facility.isActive())
               // .images(facility.getImages().stream().map(Image::getPath).toList())
                .workDays(facility.getWorkDays().stream().map(WorkDayDto::convertToDto).toList())
                .disciplines(facility.getDisciplines().stream().map(DisciplineDto::convertToDto).toList())
                .build();
    }

    public Facility convertToModel() {
        return Facility.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .createdAt(getCreatedAt())
                .address(getAddress())
                .city(getCity())
                .totalRating(getTotalRating())
                .active(isActive())
                .workDays(getWorkDays().stream().map(WorkDayDto::convertToModel).toList())
                .disciplines(getDisciplines().stream().map(DisciplineDto::convertToModel).toList())
                .build();
    }
}
