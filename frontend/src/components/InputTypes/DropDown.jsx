import PropTypes from "prop-types";
import { useState } from "react";
import "./inputTypes.css";

export default function DropDown({name, options, isMust, value, onChange}) {

    const handleSelect = (e) => {
        onChange(e.target.value);
    };

    return (
        <div className="dropdown-input">
            <label className="input-label"> {name} </label>
            <br />
            <select value={value} onChange={handleSelect} required = {isMust} className="input-value input">
                <option value="">Select a value</option>
                {options.map((option, index) => (
                    <option key={index} value={option}>
                        {option}
                    </option>
                ))}
            </select>
        </div>
    );
}

DropDown.propTypes = {
    name: PropTypes.string.isRequired,
    options: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
    isMust: PropTypes.bool,
    onChange: PropTypes.func,
    value: PropTypes.string,
};

DropDown.defaultProps = {
    isMust: true,
};