import React, { useState } from 'react';
import FilterSection from './FilterSection';
import { locationOptions, employmentTypes, jobLevels, minimumSalary } from '../../constants/filterOptions';
import './Filters.css';

function Filters({ filters, onFilterChange }) {


    return (
        <div className="filters">
            <h3>Filters</h3>

            <FilterSection
                title="Work Location"
                options={locationOptions}
                name="work location"
                selectedValue={filters.workLocation}
                handleFilterChange={ (value) => onFilterChange("workLocation", value) }
            />

            {/* full-time, part-time, ...  */}
            <FilterSection
                title="Type of Employment"
                options={employmentTypes}
                name="employment"
                selectedValue={filters.employmentType}
                handleFilterChange={ (value) => onFilterChange("employmentType", value) }
            />

            {/* Entry, Junior, Mid, Senior, Executive */}
            <FilterSection
                title="Experience Level"
                options={jobLevels}
                name="level"
                selectedValue={filters.jobLevel}
                handleFilterChange={ (value) => onFilterChange("jobLevel", value) }
            />

            {/* salary may be needed to be converted into a slider on next milestone */}
            <FilterSection
                title="Minimum Salary"
                options={minimumSalary}
                name="Min salary"
                selectedValue={filters.salary}
                handleFilterChange={ (value) => onFilterChange("salary", value) }
            />
        </div>
    );
}

export default Filters;