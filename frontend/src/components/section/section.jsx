import React, { useEffect, useState } from "react";
import "./section.css";

export default function Section({ sectionData, sectionChange, errors, save, cancel }) {
    const [render, setRender] = useState(false);
    const [editedFields, setEditedFields] = useState(sectionData.sectionFields);
    const [isEditing, setIsEditing] = useState(false);

    const handleFieldChange = (e, fieldIndex) => {
        const updatedFieldValue = e.target.value;
        setEditedFields((prevFields) => {
            const updatedFields = [...prevFields];
            updatedFields[fieldIndex] = { ...updatedFields[fieldIndex], fieldValue: updatedFieldValue };
            return updatedFields;
        });

        // Now use the fieldName as the name for sectionChange
        sectionChange(sectionData.sectionName, editedFields[fieldIndex].fieldName, updatedFieldValue);
    };

    useEffect(() => {
        // Synchronize editedFields with sectionData when sectionData changes
        setEditedFields(sectionData.sectionFields);
    }, [sectionData]);

    useEffect(() => {
        if (Object.keys(errors).length > 0) {
            setIsEditing(true); // Keep editing mode active if there are errors
        }
    }, [errors]);

    const handleSave = () => {
        save(editedFields);
        if (Object.keys(errors).length === 0) {
            setIsEditing(false); // Exit edit mode if no errors
        } else {
            setIsEditing(true); // Stay in edit mode if there are errors
        }
        setRender(!render);
    };

    const handleCancel = () => {
        setIsEditing(false); // Exit edit mode if canceled
        cancel();
        setEditedFields(sectionData.sectionFields); // Reset fields to original data
        setRender(!render);
    };

    return (
        <div className="section">
            <div className="section-header">
                <div className="section-name">{sectionData.sectionName}</div>
                <div className="section-edit">
                    {!isEditing && (
                        <i
                            className="fa-solid fa-pen-to-square"
                            onClick={() => setIsEditing(true)}
                        ></i>
                    )}
                </div>
            </div>
            <div className="section-fields">
                {editedFields.map((field, index) => (
                    <div className="section-field" key={index}>
                        <div className="field-name">{field.fieldName}</div>
                        <div className="input-contianer">
                            <input
                                type={field.fieldType}
                                name={field.fieldName} // Add the name attribute here
                                className={`field-value ${
                                    errors && errors[field.fieldName] ? "error-field" : ""
                                } ${isEditing ? "editable" : ""}`}
                                value={field.fieldValue}
                                onChange={(e) => handleFieldChange(e, index)}
                                disabled={!isEditing} // Disable input when not in edit mode
                                minLength={field.minLength}
                                maxLength={field.maxLength}
                            />
                            {errors && errors[field.fieldName] && (
                                <div className="error">{errors[field.fieldName]}</div>
                            )}
                        </div>
                    </div>
                ))}
            </div>
            {isEditing && (
                <div className="section-options">
                    <div onClick={handleSave} className="save-button">
                        Save
                    </div>
                    <div onClick={handleCancel} className="cancel-button">
                        Cancel
                    </div>
                </div>
            )}
        </div>
    );
}
