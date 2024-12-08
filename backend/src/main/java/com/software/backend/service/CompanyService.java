package com.software.backend.service;

import com.software.backend.dto.CompanyDto;
import com.software.backend.entity.Company;
import com.software.backend.entity.User;
import com.software.backend.repository.CompanyRepository;
import com.software.backend.mapper.CompanyMapper;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CompanyMapper companyMapper;

    public CompanyDto getCompanyInfo(String companyUsername) {
        User user = userRepository.findByUsername(companyUsername).orElse(null);
        if (user == null) return null;

        Company company = user.getCompany();
        if (company == null) return null;

        return companyMapper.toCompanyDTO(company);
    }

    public CompanyDto updateCompanyInfo(String companyUsername, CompanyDto newCompanyDto){
        User user = userRepository.findByUsername(companyUsername).orElse(null);
        if (user == null) return null;

        Company company = user.getCompany();
        if (company == null) return null;

        return getCompanyDto(newCompanyDto, company);
    }

    private CompanyDto getCompanyDto(CompanyDto newCompanyDto, Company company) {

        if(newCompanyDto.getName() != null) company.setName(newCompanyDto.getName());
        if(newCompanyDto.getWebsite() != null) company.setWebsite(newCompanyDto.getWebsite());
        if(newCompanyDto.getLocation() != null) company.setLocation(newCompanyDto.getLocation());
        if(newCompanyDto.getOverview() != null) company.setOverview(newCompanyDto.getOverview());

        companyRepository.save(company);
        return companyMapper.toCompanyDTO(company);
    }

}
