package com.ftn.sitpass.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime createdAt;

    private Integer exerciseCount;

    private Boolean hidden;

    @ManyToOne
    private User author;

    private Comment comment;

    @OneToOne
    private Rate rating;

    @ManyToOne
    private Facility facility;

}