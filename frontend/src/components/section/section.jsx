import React, { useState } from "react";
import "./section.css";

export default function Section({ sectionData, sectionChange, errors, save, cancel }) {
    const [isEditable, setIsEditable] = useState(false);  // State to track edit mode
    const [editedFields, setEditedFields] = useState(sectionData.sectionFields);

    const handleFieldChange = (e, fieldIndex) => {
        const updatedFieldValue = e.target.value;
        setEditedFields((prevFields) => {
            const updatedFields = [...prevFields];
            updatedFields[fieldIndex] = { ...updatedFields[fieldIndex], fieldValue: updatedFieldValue };
            return updatedFields;
        });
    };

    const handleSave = () => {
        save(editedFields);  // Save the edited values
        setIsEditable(false); // Exit edit mode after saving
    };

    const handleCancel = () => {
        cancel();  // Cancel the changes and revert to original values
        setEditedFields(sectionData.sectionFields); // Revert to the original field values
        setIsEditable(false); // Exit edit mode
    };

    const handleEditToggle = () => {
        setIsEditable((prevState) => !prevState);  // Toggle edit mode
    };

    return (
        <div className="section">
            <div className="section-header">
                <div className="section-name">
                    {sectionData.sectionName}
                </div>
                <div className="section-options">
                        {isEditable ? (
                            <>
                                <div className="cancel-button" onClick={handleCancel}>Cancel</div>
                                <div className="save-button" onClick={handleSave}>Save</div>
                            </>
                        ) : (
                            <i className="fa-solid fa-pen-to-square" onClick={handleEditToggle}></i> // Keeps the edit icon when not in edit mode
                        )}
                </div>
            </div>
            <div className="section-fields">
                {editedFields.map((field, index) => (
                    <div className="section-field" key={index}>
                        <div className="field-name">{field.fieldName}</div>
                        <div className="input">
                            <input
                                className={`field-value ${errors && errors[field.fieldName] && 'error-field'}`}
                                value={field.fieldValue}
                                name={field.fieldName}
                                onChange={(e) => handleFieldChange(e, index)}
                                disabled={!isEditable}  // Disable input fields if not in edit mode
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
