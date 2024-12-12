package com.software.backend.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "banned_email")
public class BannedEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id")
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;

    @CreationTimestamp
    @Column(name = "banned_at", nullable = false, updatable = false)
    private LocalDateTime bannedAt;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;
}
