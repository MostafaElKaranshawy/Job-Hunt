package com.software.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Entity
@Data
@Table(name = "applicant")
public class Applicant{

    @Id
    @Column(name = "applicant_id")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "applicant_id", nullable = false, referencedColumnName = "id")
    @MapsId
    private User user;

    @Column( nullable = false )
    private String firstName;

    @Column( nullable = false )
    private String lastName;

    private String phoneNumber;

    private String address;

    private String city;

    private String state;

    private String country;


    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educationList;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experienceList;


    @ManyToMany
    @JoinTable(
        name = "applicant_skill",
        joinColumns = @JoinColumn(name = "applicant_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;
}


/*
* address   (done)
* city   (done)
* state   (done)
* country   (done)
* resume  (to be put after specifying how exactly will it be dealt with in java)
*
*
*
* */
