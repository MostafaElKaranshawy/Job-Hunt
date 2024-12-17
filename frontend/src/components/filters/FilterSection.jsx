import React from 'react';
import './FilterSection.css';

function FilterSection({ title, options, name, selectedValue, handleFilterChange }) {

    const handleClick = (optionId) => {
        if (selectedValue === optionId) {
            handleFilterChange(""); // Deselect if already selected
        } else {
            handleFilterChange(optionId); // Otherwise, select the new value
        }
    };

    return (
        <div className="filter-section">
            <h4>{title}</h4>
            {options.map((option) => (
                <label key={option.id} className="filter-option">
                    <input
                        type="radio"
                        name={name}
                        value={option.id}
                        checked={selectedValue === option.id}
                        onChange={() => handleClick(option.id)}  // added to prevent the warning
                        onClick={() => handleClick(option.id)}
                    />
                    {option.label}
                </label>
            ))}
        </div>
    );
}

export default FilterSection;