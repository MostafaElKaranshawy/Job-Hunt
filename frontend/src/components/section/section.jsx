import React, { useState } from "react";
import "./section.css";

export default function Section({ sectionData, sectionChange, errors, save, cancel }) {
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

    const handleSave = () => {
        save(editedFields);
        setIsEditing(false);  // Exit edit mode after saving
    };

    const handleCancel = () => {
        cancel();
        setIsEditing(false);  // Exit edit mode if canceled
        setEditedFields(sectionData.sectionFields);
    };

    return (
        <div className="section">
            <div className="section-header">
                <div className="section-name">{sectionData.sectionName}</div>
                <div className="section-options">
                    {isEditing ? (
                        <>
                            <button onClick={handleSave}>Save</button>
                            <button onClick={handleCancel}>Cancel</button>
                        </>
                    ) : (
                        <button onClick={() => setIsEditing(true)}>Edit</button>
                    )}
                    {/* <button onClick={() => removeSection(sectionData.sectionName, sectionData.id)}>Delete</button> */}
                </div>
            </div>
            <div className="section-fields">
                {editedFields.map((field, index) => (
                    <div className="section-field" key={index}>
                        <div className="field-name">{field.fieldName}</div>
                        <input
                            name={field.fieldName}  // Add the name attribute here
                            className={`field-value ${errors && errors[field.fieldName] && "error-field"}`}
                            value={field.fieldValue}
                            onChange={(e) => handleFieldChange(e, index)}
                            disabled={!isEditing} // Disable input when not in edit mode
                        />
                        {errors && errors[field.fieldName] && <div className="error">{errors[field.fieldName]}</div>}
                    </div>
                ))}
            </div>
        </div>
    );
}
