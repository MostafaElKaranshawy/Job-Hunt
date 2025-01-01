package com.software.backend.dto;

import com.software.backend.entity.Field;
import com.software.backend.entity.JobApplication;
import com.software.backend.entity.Section;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ApplicationResponseDTO {
    private PersonalDataDTO personalData;
    private EducationDataDTO educationData;
    private ExperienceDataDTO experienceData;
    private List<String> skillData;
    private List<SpecialFieldDTO> specialFieldsData;
    private List<SpecialSectionDTO> specialSectionsData;
    private int applicationStatus;
    private int applicationId;

    @Data
    public static class PersonalDataDTO {
        private String fullName;
        private String address;
        private String phoneNumber;
        private String personalEmail;
        private String portfolioURL;
        private String linkedInURL;
        private String dateOfBirth;
    }

    @Data
    public static class EducationDataDTO {
        private String fieldOfStudy;
        private String graduationYear;
        private String highestDegree;
        private String startYear;
        private String university;
    }

    @Data
    public static class ExperienceDataDTO {
        private String companyName;
        private String jobTitle;
        private String jobLocation;
        private String jobDescription;
        private String startDate;
        private String endDate;
        private boolean currentRule;
    }

    @Data
    public static class SpecialFieldDTO {
        private String fieldName;
        private String data;
    }

    @Data
    public static class SpecialSectionDTO {
        private String sectionName;
        private Map<String, String> data;       // FieldName, Data
    }
}
