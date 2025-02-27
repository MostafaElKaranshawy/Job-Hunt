package com.software.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"user", "jobs"})
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

    private String location; // no address?

    private String website;

    private String overview;

    @Column(name = "establishment_date")
    private LocalDate establishmentDate;


    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs;

}

/*
 *  phoneNumber if we changed our minds (as per last discussion with TA)
 */
