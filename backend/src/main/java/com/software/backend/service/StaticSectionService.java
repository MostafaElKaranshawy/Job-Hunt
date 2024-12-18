package com.software.backend.service;

import com.software.backend.dto.FieldDto;
import com.software.backend.dto.SectionDto;
import com.software.backend.entity.Section;
import com.software.backend.mapper.SectionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaticSectionService {
    @Autowired
    private SectionMapper sectionMapper;

    public Section getSection (String sectionName){
        return switch (sectionName) {
            case "Personal Information" -> getPersonalInfoSection();
            case "Education" -> getEducationSection();
            case "Experience" -> getExperienceSection();
            case "Skills" -> getSkillsSection();
            default -> null;
        };
    }


    public Section getPersonalInfoSection(){
        SectionDto personalInfo = new SectionDto();
        personalInfo.setName("Personal Information");
        personalInfo.setFields(List.of(
                new FieldDto("Full Name", "text", true, new ArrayList<>()),
                new FieldDto("Phone Number", "text", true, new ArrayList<>()),
                new FieldDto("Personal Email", "email", true, new ArrayList<>()),
                new FieldDto("Address", "textarea", true, new ArrayList<>()),
                new FieldDto("Date of Birth", "date", true, new ArrayList<>()),
                new FieldDto("LinkedIn URL", "text", false, new ArrayList<>()),
                new FieldDto("Portfolio", "text", false, new ArrayList<>())
        ));
        return sectionMapper.sectionDtoToSection(personalInfo);
    }

    public Section getEducationSection(){
        SectionDto education = new SectionDto();
        education.setName("Education");
        education.setFields(List.of(
                new FieldDto("Degree", "text", true, new ArrayList<>()),
                new FieldDto("Field of Study", "text", true, new ArrayList<>()),
                new FieldDto("University/Institution Name", "text", true, new ArrayList<>()),
                new FieldDto("Start Date", "date", true, new ArrayList<>()),
                new FieldDto("End Date", "date", true, new ArrayList<>())
        ));
        return sectionMapper.sectionDtoToSection(education);
    }

    public Section getExperienceSection(){
        SectionDto experience = new SectionDto();
        experience.setName("Experience");
        experience.setFields(List.of(
                new FieldDto("Job Title", "text", true, new ArrayList<>()),
                new FieldDto("Company Name", "text", true, new ArrayList<>()),
                new FieldDto("Start Date", "date", true, new ArrayList<>()),
                new FieldDto("End Date", "date", true, new ArrayList<>()),
                new FieldDto("Job Description", "textarea", false, new ArrayList<>())
        ));
        return sectionMapper.sectionDtoToSection(experience);
    }

    public Section getSkillsSection() {
        SectionDto skills = new SectionDto();
        skills.setName("Skills");
        skills.setFields(List.of(
                new FieldDto("Skill", "text", true, new ArrayList<>())
        ));
        return sectionMapper.sectionDtoToSection(skills);
    }
}
