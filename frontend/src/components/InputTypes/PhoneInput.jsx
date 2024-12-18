import PropTypes from "prop-types";
import { useState } from "react";
import "./inputTypes.css";

export default function PhoneInput({ name, isMust, value, onChange}) {

    function handlePhoneChange(e) {
        onChange(e.target.value);
    }

    return (
        <div className="tel-input">
            <label className="input-label">{name}</label>
            <br />
            <input  type="tel"  value={value}  onChange={handlePhoneChange} 
                placeholder="Enter Phone Number" required={isMust} pattern="^\+?[1-9][0-9\s\-\(\)]{7,16}$" 
                title="Enter a valid phone number (e.g., +1234567890)" />
        </div>
    );
}

PhoneInput.propTypes = {
    name: PropTypes.string.isRequired,
    isMust: PropTypes.bool,
    onChange: PropTypes.func,
    value: PropTypes.string,
};

PhoneInput.defaultProps = {
    isMust: true,
};
