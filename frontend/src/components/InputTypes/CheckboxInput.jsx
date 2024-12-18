import PropTypes from "prop-types";
import { useState, useEffect } from "react";
import "./inputTypes.css";

export default function CheckboxInput({ name, options, value, onChange }) {
    const [myBox, setBox] = useState(value || []);

    useEffect(() => {
        setBox(value || []);
    }, [value]);

    const handleBox = (e) => {
        const optionValue = e.target.value;
        let updatedBox;
        if (myBox.includes(optionValue)) {
            updatedBox = myBox.filter((box) => box !== optionValue); // Remove value if already selected
        } else {
            updatedBox = [...myBox, optionValue]; // Add value if not already selected
        }
        setBox(updatedBox);
        onChange(updatedBox);
    };

    return (
        <>
            <label className="input-label">{name}</label>
            <br />
            <div className="checkbox-group">
                {options.map((option, index) => (
                    <label className="input-label" key={index}>
                        <input
                            type="checkbox"
                            value={option}
                            checked={myBox.includes(option)}
                            onChange={handleBox}
                        />
                        {option}
                    </label>
                ))}
            </div>
        </>
    );
}

CheckboxInput.propTypes = {
    name: PropTypes.string.isRequired,
    options: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
    value: PropTypes.arrayOf(PropTypes.string),
    onChange: PropTypes.func.isRequired,
};

CheckboxInput.defaultProps = {
    value: [],
};