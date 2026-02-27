package com.ftn.sitpass.dto.ues;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FacilitySearchHitDto {
    private Long facilityId;
    private String name;
    private String description;
    private String summary;
}
