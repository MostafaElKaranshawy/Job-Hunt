import PropTypes from "prop-types";
import { useState } from "react";
import "./inputTypes.css";

export default function SimpleText({ name, isMust, value, onChange }) {
    
    function handleTextChange(e) {
        onChange(e.target.value);
    }

    return (
        <div className="simple-text-input">
            <label className="input-label">{name}</label>
            <br />
            <input type="text" value={value} onChange={handleTextChange}
                placeholder="Enter your text" maxLength={30} required={isMust} />
        </div>
    );
}

SimpleText.propTypes = {
    name: PropTypes.string.isRequired,
    isMust: PropTypes.bool,
    onChange: PropTypes.func,
    value: PropTypes.string,
};

SimpleText.defaultProps = {
    isMust: true,
};
