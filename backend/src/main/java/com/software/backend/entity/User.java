package com.software.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.software.backend.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Applicant applicant;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Company company;

    @Column(
        unique = true,
        nullable = false
    )
    private String username;


    @Column(
        unique = true,
        nullable = false
    )
    private String email;


    @Column( nullable = false )
    private String password;

    @Column(
        name = "user_type",
        nullable = false
    )
    @Enumerated(EnumType.STRING)
    private UserType userType;


    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    @Column(name = "google_client_id")
    private String googleClientId;


    @Column(name = "is_banned")
    private Boolean isBanned;
}

