package com.ftn.sitpass.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Facility implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    @Temporal(TemporalType.DATE)
    private LocalDate createdAt;

    private String address;

    private String city;

    private Double totalRating;

    private boolean active;

    @OneToMany(mappedBy = "facility")
    private List<Image> images;

    @OneToMany(mappedBy = "facility")
    private List<WorkDay> workDays;

    @OneToMany(mappedBy = "facility")
    private List<Discipline> disciplines;

}