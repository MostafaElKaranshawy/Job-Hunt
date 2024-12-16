package com.software.backend.mapper;

import com.software.backend.dto.CompanyDto;
import com.software.backend.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CompanyMapper {
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    CompanyDto toCompanyDTO(Company company);
}

