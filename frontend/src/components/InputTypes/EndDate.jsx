import React from "react";
import PropTypes from "prop-types";
import "./inputTypes.css";
import CheckboxInput from "./CheckboxInput";

export default function EndDate({ name, value, onChange, currentRule, onCurrentRuleChange }) {
    const handleDateChange = (e) => {
        onChange(e.target.value);
    };

    const handleCurrentRuleChange = (updatedBox) => {
        const isCurrentRule = updatedBox.includes("Current Rule");
        onCurrentRuleChange(isCurrentRule);
        if (isCurrentRule) {
            onChange(""); // Clear the date if current rule is checked
        }
    };

    return (
        <div className="date-input">
            {!currentRule && (
                <>
                <label className="input-label">{name}</label>
                <br />
                <input
                    type="date"
                    value={value}
                    onChange={handleDateChange}
                />
                </>
            )}
            <CheckboxInput
                value={currentRule ? ["Current Rule"] : []}
                name=""
                options={["Current Rule"]}
                onChange={handleCurrentRuleChange}
            />
        </div>
    );
}

EndDate.propTypes = {
    name: PropTypes.string.isRequired,
    value: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    currentRule: PropTypes.bool.isRequired,
    onCurrentRuleChange: PropTypes.func.isRequired,
};