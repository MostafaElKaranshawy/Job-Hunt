import PropTypes from "prop-types";
import { useState } from "react";
import "./inputTypes.css";

export default function TextArea({ name, isMust,value, onChange }) {

    function handleTextAreaChange(e) {
        onChange(e.target.value);
    }

    return (
        <div className="textArea-input">
            <label className="input-label">{name}</label>
            <br />
            <textarea value={value} onChange={handleTextAreaChange} 
                placeholder="Enter the information" required={isMust} />
        </div>
    );
}

TextArea.propTypes = {
    name: PropTypes.string.isRequired,
    isMust: PropTypes.bool,
    onChange: PropTypes.func,
    value: PropTypes.string,
};

TextArea.defaultProps = {
    isMust: true, 
};
