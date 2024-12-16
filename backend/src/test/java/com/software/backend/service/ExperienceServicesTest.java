package com.software.backend.service;

import com.software.backend.dto.ExperienceDTO;
import com.software.backend.entity.Applicant;
import com.software.backend.entity.Experience;
import com.software.backend.mapper.ExperienceMapper;
import com.software.backend.repository.ApplicantRepository;
import com.software.backend.repository.ExperienceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExperienceServicesTest {

    @Mock
    private ExperienceRepository experienceRepo;

    @Mock
    private ApplicantRepository applicantRepo;

    @Mock
    private ExperienceMapper mapper;

    @InjectMocks
    private ExperienceServices service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addExperience_ValidApplicantId_ReturnsTrue() {
        Integer applicantId = 1;
        ExperienceDTO dto = new ExperienceDTO();
        Applicant applicant = new Applicant();
        Experience experience = new Experience();

        when(applicantRepo.findById(applicantId)).thenReturn(Optional.of(applicant));
        when(mapper.toEntity(dto)).thenReturn(experience);

        boolean result = service.addExperience(applicantId, dto);

        assertTrue(result);
        verify(applicantRepo,times(1)).findById(applicantId);
        verify(mapper,times(1)).toEntity(dto);
        verify(experienceRepo,times(1)).save(experience);
    }

    @Test
    void addExperience_InvalidApplicantId_ReturnsFalse() {
        Integer applicantId = 1;
        ExperienceDTO dto = new ExperienceDTO();

        when(applicantRepo.findById(applicantId)).thenReturn(Optional.empty());

        boolean result = service.addExperience(applicantId, dto);

        assertFalse(result);

        verify(applicantRepo, times(1)).findById(applicantId);
        verifyNoInteractions(mapper);
        verifyNoInteractions(experienceRepo);
    }

    @Test
    void getExperience_ValidApplicantId_ReturnsExperienceList() {
        Integer applicantId = 1;
        Applicant applicant = new Applicant();
        Experience experience = new Experience();
        ExperienceDTO dto = new ExperienceDTO();
        List<Experience> experienceList = List.of(experience);

        when(applicantRepo.findById(applicantId)).thenReturn(Optional.of(applicant));
        when(experienceRepo.findByApplicant(applicant)).thenReturn(experienceList);
        when(mapper.toDTO(experience)).thenReturn(dto);

        List<ExperienceDTO> result = service.getExperience(applicantId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
        verify(applicantRepo,times(1)).findById(applicantId);
        verify(experienceRepo,times(1)).findByApplicant(applicant);
        verify(mapper,times(1)).toDTO(experience);
    }

    @Test
    void getExperience_InvalidApplicantId_ThrowsException() {
        Integer applicantId = 1;

        when(applicantRepo.findById(applicantId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getExperience(applicantId));

        verify(applicantRepo,times(1)).findById(applicantId);
        verifyNoInteractions(experienceRepo);
        verifyNoInteractions(mapper);
    }

    @Test
    void updateExperience_ValidExperienceId_ReturnsTrue() {
        Integer experienceId = 1;
        ExperienceDTO dto = new ExperienceDTO();
        Experience experience = new Experience();

        when(experienceRepo.findById(experienceId)).thenReturn(Optional.of(experience));

        boolean result = service.updateExperience(experienceId, dto);

        assertTrue(result);
        verify(experienceRepo,times(1)).findById(experienceId);
        verify(mapper,times(1)).updateEntityFromDTO(dto, experience);
        verify(experienceRepo,times(1)).save(experience);
    }

    @Test
    void updateExperience_InvalidExperienceId_ReturnsFalse() {
        Integer experienceId = 1;
        ExperienceDTO dto = new ExperienceDTO();

        when(experienceRepo.findById(experienceId)).thenReturn(Optional.empty());

        boolean result = service.updateExperience(experienceId, dto);

        assertFalse(result);

        verify(experienceRepo, times(1)).findById(experienceId);
        verifyNoMoreInteractions(experienceRepo);
        verifyNoInteractions(mapper);
    }

    @Test
    void deleteExperience_ValidExperienceId_ReturnsTrue() {
        Integer experienceId = 1;
        Experience experience = new Experience();

        when(experienceRepo.findById(experienceId)).thenReturn(Optional.of(experience));

        boolean result = service.deleteExperience(experienceId);

        assertTrue(result);
        verify(experienceRepo,times(1)).findById(experienceId);
        verify(experienceRepo,times(1)).delete(experience);
    }

    @Test
    void deleteExperience_InvalidExperienceId_ReturnsFalse() {
        Integer experienceId = 1;

        when(experienceRepo.findById(experienceId)).thenReturn(Optional.empty());

        boolean result = service.deleteExperience(experienceId);

        assertFalse(result);

        verify(experienceRepo, times(1)).findById(experienceId);
        verifyNoMoreInteractions(experienceRepo);
    }
}
