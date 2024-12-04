package com.software.backend.service;

import com.software.backend.dto.EducationDTO;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.Education;
import com.software.backend.mapper.EducationMapper;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.EducationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EducationServicesTest {

    @InjectMocks
    private EducationServices service;

    @Mock
    private ApplicantRepository ApplicantRepo;

    @Mock
    private EducationRepository EducationRepo;

    @Mock
    private EducationMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes mocks and injects them into the @InjectMocks instance
    }

    @Test
    void addEducation_shouldReturnTrueWhenApplicantExists() {
        Integer applicantId = 1;
        EducationDTO dto = new EducationDTO();
        Applicant applicant = new Applicant();
        Education education = new Education();

        when(ApplicantRepo.findById(applicantId)).thenReturn(Optional.of(applicant));
        when(mapper.toEntity(dto)).thenReturn(education);
        when(EducationRepo.save(education)).thenReturn(education);

        boolean result = service.addEducation(applicantId, dto);

        assertTrue(result);
        verify(EducationRepo, times(1)).save(education);
    }

    @Test
    void addEducation_shouldReturnFalseWhenApplicantDoesNotExist() {
        Integer applicantId = 1;
        EducationDTO dto = new EducationDTO();

        when(ApplicantRepo.findById(applicantId)).thenReturn(Optional.empty());

        boolean result = service.addEducation(applicantId, dto);

        assertFalse(result);
        verify(EducationRepo, never()).save(any());
    }

    @Test
    void getEducation_shouldReturnListOfEducationDTOs() {
        Integer applicantId = 1;
        Applicant applicant = new Applicant();
        Education education = new Education();
        EducationDTO educationDTO = new EducationDTO();

        when(ApplicantRepo.findById(applicantId)).thenReturn(Optional.of(applicant));
        when(EducationRepo.findByApplicant(applicant)).thenReturn(Collections.singletonList(education));
        when(mapper.toDTO(education)).thenReturn(educationDTO);

        var result = service.getEducation(applicantId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(EducationRepo, times(1)).findByApplicant(applicant);
    }

    @Test
    void updateEducation_shouldReturnTrueWhenEducationExists() {
        Integer educationId = 1;
        EducationDTO updatedEducationDTO = new EducationDTO();
        Education educationToUpdate = new Education();

        when(EducationRepo.findById(educationId)).thenReturn(Optional.of(educationToUpdate));
        doNothing().when(mapper).updateEntityFromDTO(updatedEducationDTO, educationToUpdate);

        boolean result = service.updateEducation(educationId, updatedEducationDTO);

        assertTrue(result);
        verify(EducationRepo, times(1)).save(educationToUpdate);
    }

    @Test
    void updateEducation_shouldReturnFalseWhenEducationDoesNotExist() {
        Integer educationId = 1;
        EducationDTO updatedEducationDTO = new EducationDTO();

        when(EducationRepo.findById(educationId)).thenReturn(Optional.empty());

        boolean result = service.updateEducation(educationId, updatedEducationDTO);

        assertFalse(result);
        verify(EducationRepo, never()).save(any());
    }

    @Test
    void deleteEducation_shouldReturnTrueWhenEducationExists() {
        Integer educationId = 1;
        Education education = new Education();

        when(EducationRepo.findById(educationId)).thenReturn(Optional.of(education));

        boolean result = service.deleteEducation(educationId);

        assertTrue(result);
        verify(EducationRepo, times(1)).delete(education);
    }

    @Test
    void deleteEducation_shouldReturnFalseWhenEducationDoesNotExist() {
        Integer educationId = 1;

        when(EducationRepo.findById(educationId)).thenReturn(Optional.empty());

        boolean result = service.deleteEducation(educationId);

        assertFalse(result);
        verify(EducationRepo, never()).delete(any());
    }
}
