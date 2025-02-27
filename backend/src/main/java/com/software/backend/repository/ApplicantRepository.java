package com.software.backend.repository;

import com.software.backend.entity.Applicant;
import com.software.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Integer> {
    Optional<Applicant> findByUser_username(String userUsername);

    String user(User user);
}