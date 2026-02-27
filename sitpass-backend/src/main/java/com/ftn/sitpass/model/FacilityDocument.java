package com.ftn.sitpass.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FacilityDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String objectKey;

    private String originalFileName;

    private String contentType;

    @Column(columnDefinition = "TEXT")
    private String extractedText;

    @OneToOne
    @JoinColumn(name = "facility_id", unique = true)
    private Facility facility;
}
