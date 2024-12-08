package com.software.backend.repository;

import com.software.backend.dto.CompanyDto;
import com.software.backend.entity.Company;
import com.software.backend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompanyRepository extends JpaRepository<Company, User> {

}