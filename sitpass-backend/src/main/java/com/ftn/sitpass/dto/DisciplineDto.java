package com.ftn.sitpass.dto;

import com.ftn.sitpass.model.Discipline;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineDto {

    private Long id;

    private Long facilityId;

    private String name;

    public static DisciplineDto convertToDto(Discipline discipline) {
        return DisciplineDto.builder()
                .id(discipline.getId())
                .name(discipline.getName())
                .facilityId(discipline.getFacility().getId())
                .build();
    }

    public Discipline convertToModel() {
        return Discipline.builder()
                .id(getId())
                .name(getName())
                .build();
    }
}
