import React from "react";
import "./section.css";

export default function Section({ sectionData, sectionChange, errors }) {
    const handleFieldChange = (e, fieldIndex) => {
        const updatedFieldValue = e.target.value;
        sectionChange(
            sectionData.sectionName,
            sectionData.sectionFields[fieldIndex].fieldName,
            updatedFieldValue
        );
    };

    return (
        <div className="section">
            <div className="section-name">{sectionData.sectionName}</div>
            <div className="section-fields">
                {sectionData.sectionFields.map((field, index) => (
                    <div className="section-field" key={index}>
                        <div className="field-name">{field.fieldName}</div>
                        <div className="input">
                            <input
                                className={`field-value ${errors && errors[field.fieldName] && 'error-field'}`}
                                value={field.fieldValue}
                                name={field.fieldName}
                                onChange={(e) => handleFieldChange(e, index)}
                            />
                            {errors && errors[field.fieldName] && (
                                <div className="error">{errors[field.fieldName]}</div>
                            )}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
