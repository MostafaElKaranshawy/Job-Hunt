import PropTypes from "prop-types";
import { useState } from "react";
import "./inputTypes.css";

export default function EmailInput({ name, isMust, value, onChange }) {

    function handleEmailChange(e) {
        onChange(e.target.value);
    }

    return (
        <div className="email-input">
            <label>{name}</label>
            <br />
            <input type="email" value={value} onChange={handleEmailChange}
                placeholder="Enter Email Address" maxLength={50} required={isMust} />
        </div>
    );
}

EmailInput.propTypes = {
    name: PropTypes.string.isRequired,
    isMust: PropTypes.bool,
    onChange: PropTypes.func,
    value: PropTypes.string,
};

EmailInput.defaultProps = {
    isMust: true,
};
