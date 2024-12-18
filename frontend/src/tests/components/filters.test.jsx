import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import Filters from '../../components/filters/Filters';
import { locationOptions, employmentTypes, jobLevels, minimumSalary } from '../../constants/filterOptions';

import { expect, vi, test } from 'vitest';



const mockFilters = {
    location: '',
    employmentType: '',
    jobLevel: '',
    salary: '',
};

describe('Filters Component', () => {
    let mockOnFilterChange = vi.fn();

    beforeEach(() => {
        mockOnFilterChange.mockClear();
    });

    test('renders all filter sections with correct titles', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );

        expect(screen.getByText('Filters')).toBeInTheDocument();
        expect(screen.getByText('Work Location')).toBeInTheDocument();
        expect(screen.getByText('Type of Employment')).toBeInTheDocument();
        expect(screen.getByText('Experience Level')).toBeInTheDocument();
        expect(screen.getByText('Minimum Salary')).toBeInTheDocument();
    });

    // check if all filter options are rendered

    test('renders filter options for "Location"', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );

        for (const option of locationOptions) {
            expect(screen.getByLabelText(option.label)).toBeInTheDocument();
        }

        // instead of the above for loop, you can also use the following: 

        // expect(screen.getByLabelText('Remote')).toBeInTheDocument();
        // expect(screen.getByLabelText('Onsite')).toBeInTheDocument();
        // expect(screen.getByLabelText('Hybrid')).toBeInTheDocument();
    });

    test('renders filter options for "Employment Type"', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );
    
        for (const option of employmentTypes) {
            expect(screen.getByLabelText(option.label)).toBeInTheDocument();
        }
    
    });
    
    test('renders filter options for "Job Levels"', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );
    
        for (const option of jobLevels) {
            expect(screen.getByLabelText(option.label)).toBeInTheDocument();
        }
    
    });
    
    test('renders filter options for "Minimum Salary"', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );
    
        for (const option of minimumSalary) {
            expect(screen.getByLabelText(option.label)).toBeInTheDocument();
        }
    
    });
    

    // no preselections

    test('no preselections for "Location" options', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );

        for (const option of locationOptions) {
            const locationOption = screen.getByLabelText(option.label);
            expect(locationOption).not.toBeChecked();
        }


        // const remoteOption = screen.getByLabelText('Remote');
        // expect(remoteOption).not.toBeChecked();

        // const onsiteOption = screen.getByLabelText('Onsite');
        // expect(onsiteOption).not.toBeChecked();

        // const hybridOption = screen.getByLabelText('Hybrid');
        // expect(hybridOption).not.toBeChecked();
        
    });

    test('no preselections for "Employment Type" options', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );
    
        for (const option of employmentTypes) {
            const employmentOption = screen.getByLabelText(option.label);
            expect(employmentOption).not.toBeChecked();
        }
    });
    
    test('no preselections for "Job Levels" options', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );
    
        for (const option of jobLevels) {
            const jobLevelOption = screen.getByLabelText(option.label);
            expect(jobLevelOption).not.toBeChecked();
        }
    });

    test('no preselections for "Minimum Salary" options', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );
    
        for (const option of minimumSalary) {
            const salaryOption = screen.getByLabelText(option.label);
            expect(salaryOption).not.toBeChecked();
        }
    });
    

    // changing filter options

    test('calls onFilterChange when a new filter option is selected from emplymentTypes', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );

        const partTimeOption = employmentTypes.find(option => option.id === 'PART_TIME');

        const selectedOption = screen.getByLabelText('Part-time');
        fireEvent.click(selectedOption);

        expect(mockOnFilterChange).toHaveBeenCalledWith('employmentType', partTimeOption.id);
    });

    test('calls onFilterChange when a new filter option is selected from locations', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );

        const remoteOption = locationOptions.find(option => option.id === 'REMOTE');

        const selectedOption = screen.getByLabelText('Remote');
        fireEvent.click(selectedOption);

        expect(mockOnFilterChange).toHaveBeenCalledWith('workLocation', remoteOption.id);
    });

    test('calls onFilterChange when a new filter option is selected from job levels', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );
    
        const jobLevelOption = jobLevels.find(option => option.id === 'JUNIOR_LEVEL');
    
        // Find the job level option by its label
        const selectedOption = screen.getByLabelText('Junior');
        fireEvent.click(selectedOption);
    
        // Verify onFilterChange was called with correct arguments
        expect(mockOnFilterChange).toHaveBeenCalledWith('jobLevel', jobLevelOption.id);
    });

    test('calls onFilterChange when a new filter option is selected from minimum salary', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );
    
        const salaryOption = minimumSalary.find(option => option.id === '10000');
    
        // Find the salary option by its label
        const selectedOption = screen.getByLabelText('10K');
        fireEvent.click(selectedOption);
    
        // Verify onFilterChange was called with correct arguments
        expect(mockOnFilterChange).toHaveBeenCalledWith('salary', salaryOption.id);
    });
    

    // deselecting filter options

    test('deselects the filter if the same option is clicked (employment type)', () => {
        render(
            <Filters
                filters={{ ...mockFilters, employmentType: 'FULL_TIME' }}
                onFilterChange={mockOnFilterChange}
            />
        );

        const selectedOption = screen.getByLabelText('Full-time');
        fireEvent.click(selectedOption);

        expect(mockOnFilterChange).toHaveBeenCalledWith('employmentType', '');
    });

    test('deselects the filter if the same option is clicked (location)', () => {
        render(
            <Filters
                filters={{ ...mockFilters, workLocation: 'REMOTE' }}
                onFilterChange={mockOnFilterChange}
            />
        );

        const selectedOption = screen.getByLabelText('Remote');
        fireEvent.click(selectedOption);

        expect(mockOnFilterChange).toHaveBeenCalledWith('workLocation', '');
    });

    test('deselects the filter if the same option is clicked (job level)', () => {
        render(
            <Filters
                filters={{ ...mockFilters, jobLevel: 'JUNIOR_LEVEL' }}
                onFilterChange={mockOnFilterChange}
            />
        );

        const selectedOption = screen.getByLabelText('Junior');
        fireEvent.click(selectedOption);

        expect(mockOnFilterChange).toHaveBeenCalledWith('jobLevel', '');
    });

    test('deselects the filter if the same option is clicked (minimum salary)', () => {
        render(
            <Filters
                filters={{ ...mockFilters, salary: '10000' }}
                onFilterChange={mockOnFilterChange}
            />
        );

        const selectedOption = screen.getByLabelText('10K');
        fireEvent.click(selectedOption);

        expect(mockOnFilterChange).toHaveBeenCalledWith('salary', '');
    });

    

    // multiple selections
    test('calls onFilterChange when "Job Levels" and "Minimum Salary" filters are selected', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );
    
        // Select "Mid-Level" for Job Levels
        const midLevelOption = screen.getByLabelText('Mid-Level');
        fireEvent.click(midLevelOption);
        expect(mockOnFilterChange).toHaveBeenCalledWith('jobLevel', 'MID_LEVEL');
    
        // Select "10000K" for Minimum Salary
        const salaryOption = screen.getByLabelText('10K');
        fireEvent.click(salaryOption);
        expect(mockOnFilterChange).toHaveBeenCalledWith('salary', '10000');
    });
    
    test('calls onFilterChange when multiple filters are selected: "Location", "Employment Type", and "Job Levels"', () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );
    
        // Select "Onsite" for Location
        const onsiteOption = screen.getByLabelText('Onsite');
        fireEvent.click(onsiteOption);
        expect(mockOnFilterChange).toHaveBeenCalledWith('workLocation', 'ONSITE');
    
        // Select "Part-time" for Employment Type
        const partTimeOption = screen.getByLabelText('Part-time');
        fireEvent.click(partTimeOption);
        expect(mockOnFilterChange).toHaveBeenCalledWith('employmentType', 'PART_TIME');
    
        // Select "Senior" for Job Levels
        const seniorOption = screen.getByLabelText('Senior');
        fireEvent.click(seniorOption);
        expect(mockOnFilterChange).toHaveBeenCalledWith('jobLevel', 'SENIOR_LEVEL');
    });
    
    test(`calls onFilterChange when all filters are selected: "Location", 
         "Employment Type", "Job Levels", and "Minimum Salary"`, () => {
        render(
            <Filters
                filters={mockFilters}
                onFilterChange={mockOnFilterChange}
            />
        );
    
        // Select "Hybrid" for Location
        const hybridOption = screen.getByLabelText('Hybrid');
        fireEvent.click(hybridOption);
        expect(mockOnFilterChange).toHaveBeenCalledWith('workLocation', 'HYBRID');
    
        // Select "Intern" for Employment Type
        const internOption = screen.getByLabelText('Intern');
        fireEvent.click(internOption);
        expect(mockOnFilterChange).toHaveBeenCalledWith('employmentType', 'INTERN');
    
        // Select "Executive" for Job Levels
        const executiveOption = screen.getByLabelText('Executive');
        fireEvent.click(executiveOption);
        expect(mockOnFilterChange).toHaveBeenCalledWith('jobLevel', 'EXECUTIVE');
    
        // Select "20000K" for Minimum Salary
        const salaryOption = screen.getByLabelText('20K');
        fireEvent.click(salaryOption);
        expect(mockOnFilterChange).toHaveBeenCalledWith('salary', '20000');
    });
    
});