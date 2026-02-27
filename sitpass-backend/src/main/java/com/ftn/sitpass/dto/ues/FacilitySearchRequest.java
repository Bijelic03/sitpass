package com.ftn.sitpass.dto.ues;

import lombok.Data;

@Data
public class FacilitySearchRequest {
    private String name;
    private String description;
    private String pdfDescription;
    private Integer minReviews;
    private Integer maxReviews;
    private Double minAvgStaff;
    private Double maxAvgStaff;
    private Double minAvgEquipment;
    private Double maxAvgEquipment;
    private Double minAvgHygiene;
    private Double maxAvgHygiene;
    private Double minAvgSpace;
    private Double maxAvgSpace;
    private Long moreLikeThisFacilityId;
    private String operator = "AND";
    private Boolean sortByNameAsc = true;
}
