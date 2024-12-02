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
        if(errors){
            setIsEditing(true);
        }
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
                <div className="section-edit">
                    {
                    !isEditing && (
                        <i className="fa-solid fa-pen-to-square" onClick={() => setIsEditing(true)}></i>
                    )}
                </div>
            </div>
            <div className="section-fields">
                {editedFields.map((field, index) => (
                    <div className="section-field" key={index}>
                        <div className="field-name">{field.fieldName}</div>
                        <div>
                            <input
                                type={field.fieldType}
                                name={field.fieldName}  // Add the name attribute here
                                className={`field-value ${errors && errors[field.fieldName] && "error-field"}`}
                                value={field.fieldValue}
                                onChange={(e) => handleFieldChange(e, index)}
                                disabled={!isEditing} // Disable input when not in edit mode
                            />
                            {errors && errors[field.fieldName] && <div className="error">{errors[field.fieldName]}</div>}
                        </div>
                    </div>
                ))}
            </div>
            {
                isEditing && (
                <div className="section-options">
                    <div onClick={handleSave} className="save-button">Save</div>
                    <div onClick={handleCancel} className="cancel-button">Cancel</div>
                </div>
            )}
        </div>
    );
}
