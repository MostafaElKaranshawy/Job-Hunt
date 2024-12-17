package com.software.backend.service;

import com.software.backend.dto.FieldDto;
import com.software.backend.dto.JobDto;
import com.software.backend.dto.SectionDto;
import com.software.backend.entity.*;
import com.software.backend.mapper.FieldMapper;
import com.software.backend.mapper.JobMapper;
import com.software.backend.repository.JobRepository;
import com.software.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private StaticSectionService staticSectionService;
    @Autowired
    private FieldMapper fieldMapper;


    public Integer createJobWithCustomForm(String companyUsername, JobDto jobDto) {
        try{
            User user = userRepository.findByUsername(companyUsername).orElse(null);
            if (user == null) throw new IllegalArgumentException("User not found for username: " + companyUsername);

            Company company = user.getCompany();
            if (company == null) throw new IllegalArgumentException("Company not found for user: " + companyUsername);

            Job job = jobMapper.jobDtoToJob(jobDto);
            job.setCompany(company);

            List<Section> sections = getSections(jobDto, job);
            List<Section> staticSections = getStaticSections(jobDto, job);
            sections.addAll(staticSections);
            job.setSections(sections);

            List<Field> fields = getFields(jobDto, job);
            job.setFields(fields);

            jobRepository.save(job);
            return job.getId();
        } catch (Exception e) {
            System.out.println("### Error : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private List<Field> getFields(JobDto jobDto, Job job) {
        List<Field> fields = new ArrayList<>();
        for (FieldDto fieldDto : jobDto.getFields()) {
            Field field = fieldMapper.fieldDtoToField(fieldDto);
            field.setJob(job);
            fields.add(field);
        }
        return fields;
    }

    private List<Section> getStaticSections(JobDto jobDto, Job job) {
        List<Section> staticSections = new ArrayList<>();
        for (String sectionName : jobDto.getStaticSections()) {
            Section section = staticSectionService.getSection(sectionName);
            if (section != null) {
                List<Field> fields = new ArrayList<>();
                for (Field field : section.getFields()) {
                    field.setSection(section);
                    field.setJob(job);
                    fields.add(field);
                }
                section.setFields(fields);
                section.setJob(job);
                staticSections.add(section);
            }
        }
        return staticSections;
    }

    private static List<Section> getSections(JobDto jobDto, Job job) {
        List<Section> sections = new ArrayList<>();
        for(SectionDto sectionDto : jobDto.getSections()) {
            Section section = new Section();
            section.setName(sectionDto.getName());
            section.setJob(job);
            List<Field> fields = new ArrayList<>();
            for (int i = 0; i < sectionDto.getLabel().size(); i++) {
                Field field = new Field();
                field.setIsRequired(sectionDto.getIsRequired().get(i));
                field.setLabel(sectionDto.getLabel().get(i));
                field.setType(sectionDto.getType().get(i));
                field.setOptions(sectionDto.getOptions().get(i));
                field.setSection(section);
                field.setJob(job);
                fields.add(field);
            }
            section.setFields(fields);
            sections.add(section);
        }
        return sections;
    }
}

