import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import SimpleText from "../InputTypes/SimpleText";
import TextArea from "../InputTypes/TextArea";
import DateInput from "../InputTypes/DateInput";
import DropDown from "../InputTypes/DropDown";
import RadioInput from "../InputTypes/RadioInput";
import CheckboxInput from "../InputTypes/CheckboxInput";
import EmailInput from "../InputTypes/EmailInput";
import PhoneInput from "../InputTypes/PhoneInput";
import URLInput from "../InputTypes/URLInput";

export default function SpecialField({ label, fieldType, fieldOptions, isMandatory, onChange }) {
    const [fieldData, setFieldData] = useState("");

    const handleChange = (value) => {
        setFieldData(value);
    };

    useEffect(() => {
        onChange(fieldData);
    }, [fieldData]);

    return (
        <div className="section">
            {fieldType === "text" && (
                <SimpleText
                    name={label}
                    value={fieldData}
                    onChange={handleChange}
                    isMust={isMandatory}
                />
            )}
            {fieldType === "textarea" && (
                <TextArea
                    name={label}
                    value={fieldData}
                    onChange={handleChange}
                    isMust={isMandatory}
                />
            )}
            {fieldType === "date" && (
                <DateInput
                    name={label}
                    value={fieldData}
                    onChange={handleChange}
                    fullDate={true}
                    isMust={isMandatory}
                />
            )}
            {fieldType === "year" && (
                <DateInput
                    name={label}
                    value={fieldData}
                    onChange={handleChange}
                    fullDate={false}
                    isMust={isMandatory}
                />
            )}
            {fieldType === "email" && (
                <EmailInput
                    name={label}
                    value={fieldData}
                    onChange={handleChange}
                    isMust={isMandatory}
                />
            )}
            {fieldType === "phone" && (
                <PhoneInput
                    name={label}
                    value={fieldData}
                    onChange={handleChange}
                    isMust={isMandatory}
                />
            )}
            {fieldType === "url" && (
                <URLInput
                    name={label}
                    value={fieldData}
                    onChange={handleChange}
                    isMust={isMandatory}
                />
            )}
            {fieldType === "dropdown" && (
                <DropDown
                    name={label}
                    options={fieldOptions}
                    value={fieldData}
                    onChange={handleChange}
                    isMust={isMandatory}
                />
            )}
            {fieldType === "radio" && (
                <RadioInput
                    name={label}
                    options={fieldOptions}
                    value={fieldData}
                    onChange={handleChange}
                />
            )}
            {fieldType === "checkbox" && (
                <CheckboxInput
                    name={label}
                    options={fieldOptions}
                    value={fieldData}
                    onChange={handleChange}
                />
            )}
            <br />
        </div>
    );
}

SpecialField.propTypes = {
    label: PropTypes.string.isRequired,
    fieldType: PropTypes.string.isRequired,
    fieldOptions: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
    isMandatory: PropTypes.bool.isRequired,
    onChange: PropTypes.func.isRequired,
};