import React, { useState } from "react";
import PropTypes from "prop-types";
import "./inputTypes.css";

export default function DateInput({ name, fullDate, isMust, value, onChange}) {
    
    const handleDateChange = (e) => {
        onChange(e.target.value);
    };

    const handleYearChange = (e) => {
        const x = e.target.value;
        if (/^\d{0,4}$/.test(x)) { 
            onChange(x);
        }
    };

    return (
        <div className="date-input">
            {fullDate ? (
                <>
                <label> {name} </label>
                <br />
                <input type="date" value={value} onChange={handleDateChange} required={isMust}/>
                </>
            ) : (
                <>
                <label> {name} </label>
                <br />
                <input className="year-input" type="text" value={value} onChange={handleYearChange} pattern="\d{4}" 
                        placeholder="YYYY" title="Enter a valid year (e.g., 2024)" required={isMust} />
                </>
            )}
        </div>
    );
}

DateInput.propTypes = {
    name: PropTypes.string.isRequired,
    fullDate: PropTypes.bool,
    isMust: PropTypes.bool,
    onChange: PropTypes.func,
    value: PropTypes.string,
};

DateInput.defaultProps = {
    fullDate: true,
    isMust: true,
};
