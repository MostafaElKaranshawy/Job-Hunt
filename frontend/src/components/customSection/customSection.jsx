import React, {useState} from "react";

import Section from "../section/section";
import './customSection.css';
export default function CustomSection({ sectionData, sectionChange, addSection, removeSection}) {
    const schoolSectionFields = ["School", "Major", "Start Date", "End Date"]
    const workSectionFields = ["Company", "Position", "Start Date", "End Date"]
    const curSectionFields = sectionData.sectionName === "Education" ? schoolSectionFields : workSectionFields;

    const [addSectionVisible, setAddSectionVisible] = useState(false);
    const [newSection, setNewSection] = useState({
        sectionName: `${sectionData.sectionName} ${sectionData.sectionSections.length + 1}`,
        sectionFields: curSectionFields.map((field) => ({
            fieldName: field,
            fieldValue: "",
        })),
    });
    const [errors, setErrors] = useState([]);
    const handleAddSection = () => {
        const newErrors = {};
        newSection.sectionFields.forEach((field) => {
            if (field.fieldValue.trim() === "") {
                newErrors[field.fieldName] = `${field.fieldName} is required.`;
            }
        });

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        // If no errors, proceed to add the section
        addSection(sectionData.sectionName, newSection); // Call parent handler
        setAddSectionVisible(false); // Hide add section UI
        setErrors({}); // Clear errors
    };
    const handleRemoveSection = (sectionName) => {
        removeSection(sectionData.sectionName, sectionName)
    }
    const addNewSection = (sectionName, fieldName, fieldValue) => {
        const updatedSections = sectionData.sectionSections.map((subSection) => {
            if (subSection.sectionName === sectionName) {
                return {
                    ...subSection,
                    sectionFields: subSection.sectionFields.map((field) =>
                        field.fieldName === fieldName
                            ? { ...field, fieldValue }
                            : field
                    ),
                };
            }
            return subSection;
        });
    
        sectionChange(sectionData.sectionName, { ...sectionData, sectionSections: updatedSections });
    };
    return (
        <div className="custom-section">
            <div className="section-header">
                <div className="section-name">{sectionData.sectionName}</div>
                <div
                    className="add-section"
                    onClick={() => {
                        setAddSectionVisible(!addSectionVisible)
                        setErrors({})
                    }}
                >
                    +
                </div>
            </div>
            <div className="section-body">
                {sectionData.sectionSections.map((section, index) => (
                    <div className="section-container">
                        <Section
                            key={index}
                            sectionData={section}
                            sectionChange={addNewSection}
                        />
                        <div className="delete-section" onClick={() => handleRemoveSection(section.sectionName)}>
                            <i className="fa-regular fa-trash-can"></i>
                        </div>
                    </div>
                ))}
                {addSectionVisible && (
                    <div className="section-added">
                        <Section
                            sectionData={newSection}
                            sectionChange={(sectionName, fieldName, fieldValue) => {
                                setNewSection((prev) => ({
                                    ...prev,
                                    sectionFields: prev.sectionFields.map((field) =>
                                        field.fieldName === fieldName
                                            ? { ...field, fieldValue }
                                            : field
                                    ),
                                }));

                                // Clear error for the field when corrected
                                setErrors((prevErrors) => {
                                    const { [fieldName]: _, ...rest } = prevErrors;
                                    return rest;
                                });
                            }}
                            errors={errors}
                        />

                        <div className="buttons">
                            <div onClick={()=>{
                                setAddSectionVisible(false);
                            }} className="cancel-button">Cancel</div>
                            <div onClick={handleAddSection} className="save-button">Save</div>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
