package com.software.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "company")
public class Company {

    @Id
    @Column(name = "company_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "company_id", nullable = false)
    @MapsId
    private User user;

    @Column(nullable = false)
    private String name;

    private String location;

    private String website;

    private String overview;

    @Column(name = "establishment_date")
    private LocalDate establishmentDate;


    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs;
}