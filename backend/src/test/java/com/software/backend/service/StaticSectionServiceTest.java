package com.software.backend.service;

import com.software.backend.dto.SectionDto;
import com.software.backend.entity.Section;
import com.software.backend.mapper.SectionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StaticSectionServiceTest {

    @Mock
    private SectionMapper sectionMapper;

    @InjectMocks
    private StaticSectionService staticSectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSection_PersonalInformation() {

        Section expectedSection = new Section();
        expectedSection.setName("Personal Information");

        when(sectionMapper.sectionDtoToSection(any(SectionDto.class))).thenReturn(expectedSection);

        Section result = staticSectionService.getSection("Personal Information");

        assertNotNull(result);
        assertEquals("Personal Information", result.getName());
        verify(sectionMapper, times(1)).sectionDtoToSection(any(SectionDto.class));
    }

    @Test
    void testGetSection_Education() {

        Section expectedSection = new Section();
        expectedSection.setName("Education");

        when(sectionMapper.sectionDtoToSection(any(SectionDto.class))).thenReturn(expectedSection);

        Section result = staticSectionService.getSection("Education");

        assertNotNull(result);
        assertEquals("Education", result.getName());
        verify(sectionMapper, times(1)).sectionDtoToSection(any(SectionDto.class));
    }

    @Test
    void testGetSection_Experience() {

        Section expectedSection = new Section();
        expectedSection.setName("Experience");

        when(sectionMapper.sectionDtoToSection(any(SectionDto.class))).thenReturn(expectedSection);

        Section result = staticSectionService.getSection("Experience");

        assertNotNull(result);
        assertEquals("Experience", result.getName());
        verify(sectionMapper, times(1)).sectionDtoToSection(any(SectionDto.class));
    }

    @Test
    void testGetSection_Skills() {

        Section expectedSection = new Section();
        expectedSection.setName("Skills");

        when(sectionMapper.sectionDtoToSection(any(SectionDto.class))).thenReturn(expectedSection);

        Section result = staticSectionService.getSection("Skills");

        assertNotNull(result);
        assertEquals("Skills", result.getName());
        verify(sectionMapper, times(1)).sectionDtoToSection(any(SectionDto.class));
    }

    @Test
    void testGetSection_InvalidSection() {
        Section result = staticSectionService.getSection("Invalid Section");

        assertNull(result);
        verify(sectionMapper, never()).sectionDtoToSection(any(SectionDto.class));
    }

}
