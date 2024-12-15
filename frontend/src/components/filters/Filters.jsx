import React, { useState } from 'react';
import FilterSection from './FilterSection';
import { locationOptions, employmentTypes, jobLevels, salaryRanges } from '../../constants/filterOptions';
import './Filters.css';

function Filters({ filters, onFilterChange }) {


    return (
        <div className="filters">
            <h3>Filters</h3>

            <FilterSection
                title="Location"
                options={locationOptions}
                name="location"
                selectedValue={filters.location}
                handleFilterChange={ (value) => onFilterChange("location", value) }
            />

            {/* full-time, part-time, ...  */}
            <FilterSection
                title="Type of employment"
                options={employmentTypes}
                name="employment"
                selectedValue={filters.employmentType}
                handleFilterChange={ (value) => onFilterChange("employmentType", value) }
            />

            {/* Entry, Junior, Mid, Senior, Executive */}
            <FilterSection
                title="Experience level"
                options={jobLevels}
                name="level"
                selectedValue={filters.jobLevel}
                handleFilterChange={ (value) => onFilterChange("jobLevel", value) }
            />

            <FilterSection
                title="Salary"
                options={salaryRanges}
                name="salary"
                selectedValue={filters.salary}
                handleFilterChange={ (value) => onFilterChange("salary", value) }
            />
        </div>
    );
}

export default Filters;