package com.ftn.sitpass.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`user`")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String name;

    private String surname;

    @Temporal(TemporalType.DATE)
    private LocalDate createdAt;

    private String phoneNumber;

    private LocalDate birthDay;

    private String address;

    private String city;

    private String zipCode;

    @OneToOne
    private Image profilePicture;

}