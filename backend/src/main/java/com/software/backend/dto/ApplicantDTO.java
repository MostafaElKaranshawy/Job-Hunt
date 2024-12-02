package com.software.backend.dto;

import com.software.backend.entity.Education;
import com.software.backend.entity.Experience;
import com.software.backend.entity.Skill;
import lombok.Data;

import java.util.List;
@Data
public class ApplicantDTO {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String country;
    private List<Education> educationList;
    private List<Experience> experienceList;
    private List<Skill> skills;

}
