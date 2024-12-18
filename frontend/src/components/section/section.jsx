import React, { useEffect, useState } from "react";
import "./section.css";

import Universities from "../../json/universities.json";
export default function Section({ sectionData, sectionChange, errors, save, cancel }) {
    const [render, setRender] = useState(false);
    const [editedFields, setEditedFields] = useState(sectionData.sectionFields);
    const [isEditing, setIsEditing] = useState(false);
    const [degrees, setDegrees] = useState([
        "Associate Degree",
        "Bachelor's Degree",
        "Master's Degree",
        "Doctoral Degree (PhD)",
        "Professional Degrees",
        "Certificate & Diploma Programs"
    ]);
    const [universities, setUniversities] = useState([]);

    useEffect(() => {
    
        setEditedFields(sectionData.sectionFields);
    }, [sectionData]);

    useEffect(() => {

        if (Object.keys(errors).length > 0) {
            setIsEditing(true); // Keep editing mode active if there are errors
        }
    }, [errors]);

    useEffect(() => {

        setUniversities(Universities);
    }, [Universities])

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


    const handleSave = async () => {

        await save(editedFields);
      
        setRender(!render);
    };

    useEffect(() => {
        if (Object.keys(errors).length === 0) {
            console.log("No errors");
            setIsEditing(false); // Exit edit mode if no errors
        }
        else {
            console.log("Errors");
            setIsEditing(true); // Stay in edit mode if there are errors
        }
    },[errors])

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
                        <div className="input-container">
                            {field.fieldName === "Degree"?
                            <select
                                name={field.fieldName}
                                className={`field-value ${
                                errors && errors[field.fieldName] ? "error-field" : ""
                                } ${isEditing ? "editable" : ""}`}
                                value={field.fieldValue}
                                onChange={(e) => handleFieldChange(e, index)}
                                disabled={!isEditing}
                            >
                                {degrees.map((degree, idx) => (
                                <option value={degree} key={idx}>
                                    {degree}
                                </option>
                                ))}
                            </select>
                            :field.fieldName === "Institution"?
                            <select
                                name={field.fieldName}
                                className={`field-value ${
                                errors && errors[field.fieldName] ? "error-field" : ""
                                } ${isEditing ? "editable" : ""}`}
                                value={field.fieldValue}
                                onChange={(e) => handleFieldChange(e, index)}
                                disabled={!isEditing}
                            >
                                {universities.map((university, idx) => (
                                <option value={university.name} key={idx}>
                                    {university}
                                </option>
                                ))}
                            </select>
                            :field.fieldName === "Description"?
                            <textarea 
                                name={field.fieldName} // Add the name attribute here
                                className={`field-value ${
                                    errors && errors[field.fieldName] ? "error-field" : ""
                                } ${isEditing ? "editable" : ""}`}
                                value={field.fieldValue}
                                onChange={(e) => handleFieldChange(e, index)}
                                disabled={!isEditing} // Disable input when not in edit mode
                                minLength={field.minLength}
                                maxLength={field.maxLength}
                            >
                                {field.fieldValue}
                            </textarea>
                            :<input
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
                            }
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
