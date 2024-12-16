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

export default function SpecialSection({ sectionName, labels, fieldType, fieldOptions, Mandatory, onChange }) {
    const [formData, setFormData] = useState({});

    const handleChange = (label, value) => {
        setFormData((prevData) => ({
            ...prevData,
            [label]: value,
        }));
    };

    useEffect(() => {
        onChange(formData);
    }, [formData]);

    return (
        <div className="section-container">
            <h2 className="section-header">{sectionName}</h2>
            <div className="section">
                {labels.map((label, index) => {
                    const type = fieldType[index];
                    const options = fieldOptions[index];
                    const isMandatory = Mandatory[index];

                    return (
                        <div key={index} className="form-group">
                            {type === "text" && (
                                <SimpleText
                                    name={label}
                                    value={formData[label] || ""}
                                    onChange={(value) => handleChange(label, value)}
                                    isMust={isMandatory}
                                />
                            )}
                            {type === "textarea" && (
                                <TextArea
                                    name={label}
                                    value={formData[label] || ""}
                                    onChange={(value) => handleChange(label, value)}
                                    isMust={isMandatory}
                                />
                            )}
                            {type === "date" && (
                                <DateInput
                                    name={label}
                                    value={formData[label] || ""}
                                    onChange={(value) => handleChange(label, value)}
                                    fullDate={true}
                                    isMust={isMandatory}
                                />
                            )}
                            {type === "year" && (
                                <DateInput
                                    name={label}
                                    value={formData[label] || ""}
                                    onChange={(value) => handleChange(label, value)}
                                    fullDate={false}
                                    isMust={isMandatory}
                                />
                            )}
                            {type === "email" && (
                                <EmailInput
                                    name={label}
                                    value={formData[label] || ""}
                                    onChange={(value) => handleChange(label, value)}
                                    isMust={isMandatory}
                                />
                            )}
                            {type === "phone" && (
                                <PhoneInput
                                    name={label}
                                    value={formData[label] || ""}
                                    onChange={(value) => handleChange(label, value)}
                                    isMust={isMandatory}
                                />
                            )}
                            {type === "url" && (
                                <URLInput
                                    name={label}
                                    value={formData[label] || ""}
                                    onChange={(value) => handleChange(label, value)}
                                    isMust={isMandatory}
                                />
                            )}
                            {type === "dropdown" && (
                                <DropDown
                                    name={label}
                                    options={options}
                                    value={formData[label] || ""}
                                    onChange={(value) => handleChange(label, value)}
                                    isMust={isMandatory}
                                />
                            )}
                            {type === "radio" && (
                                <RadioInput
                                    name={label}
                                    options={options}
                                    value={formData[label] || ""}
                                    onChange={(value) => handleChange(label, value)}
                                />
                            )}
                            {type === "checkbox" && (
                                <CheckboxInput
                                    name={label}
                                    options={options}
                                    value={formData[label] || []}
                                    onChange={(value) => handleChange(label, value)}
                                />
                            )}
                            <br />
                        </div>
                    );
                })}
            </div>
        </div>
    );
}

SpecialSection.propTypes = {
    sectionName: PropTypes.string.isRequired,
    labels: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
    fieldType: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
    fieldOptions: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.string.isRequired).isRequired).isRequired,
    Mandatory: PropTypes.arrayOf(PropTypes.bool.isRequired).isRequired,
}