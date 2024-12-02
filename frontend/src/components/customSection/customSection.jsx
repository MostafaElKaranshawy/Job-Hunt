import React, { useState } from "react";
import Section from "../section/section";
import "./customSection.css";

export default function CustomSection({ sectionData, sectionChange, addSection, editSection, removeSection, refreshCustomSections }) {
    const [addSectionVisible, setAddSectionVisible] = useState(false);
    const [newSection, setNewSection] = useState({
        sectionName: `${sectionData.sectionName} ${sectionData.sectionSections.length + 1}`,
        sectionFields: sectionData.sectionName === "Education"
            ? [
                { fieldName: "Institution", fieldValue: "" },
                { fieldName: "Degree", fieldValue: "" },
                { fieldName: "Field Of Study", fieldValue: "" },
                { fieldName: "Start Date", fieldValue: "" },
                { fieldName: "End Date", fieldValue: "" },
              ]
            : [
                { fieldName: "Title", fieldValue: "" },
                { fieldName: "Company", fieldValue: "" },
                { fieldName: "Location", fieldValue: "" },
                { fieldName: "Start Date", fieldValue: "" },
                { fieldName: "End Date", fieldValue: "" },
                { fieldName: "Description", fieldValue: "" },
              ]
    });
    const [errors, setErrors] = useState({});

    const handleFieldChange = (sectionName, fieldName, fieldValue) => {
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

    const handleSaveNewSection = () => {
        const errors = {};
        newSection.sectionFields.forEach((field) => {
            if (!field.fieldValue) {
                errors[field.fieldName] = "This field is required.";
            }
        });

        if (Object.keys(errors).length > 0) {
            setErrors(errors);
            return;
        }
        addSection(sectionData.sectionName, newSection);
        setAddSectionVisible(false);
    };

    return (
        <div className="custom-section">
            <div className="section-header">
                <div className="section-name">{sectionData.sectionName}</div>
                <div
                    className="add-section"
                    onClick={() => {
                        setAddSectionVisible(!addSectionVisible);
                        setErrors({});
                    }}
                >
                    +
                </div>
            </div>
            <div className="section-body">
                {sectionData.sectionSections.map((section, index) => (
                    <div className="section-container" key={index}>
                        <Section
                            sectionData={section}
                            sectionChange={handleFieldChange}
                            save={(editedFields) => editSection(sectionData.sectionName, section.id, editedFields)} // Pass editedFields to editSection
                            cancel={refreshCustomSections}
                        />
                        <i className="fa-solid fa-trash delete-section" onClick={()=>{
                            removeSection(sectionData.sectionName, section.id);
                        }} />
                    </div>
                ))}
                {addSectionVisible && (
                    <div className="section-added">
                        <Section
                            sectionData={newSection}
                            sectionChange={(sectionName, fieldName, fieldValue) =>
                                setNewSection((prev) => ({
                                    ...prev,
                                    sectionFields: prev.sectionFields.map((field) =>
                                        field.fieldName === fieldName
                                            ? { ...field, fieldValue }
                                            : field
                                    ),
                                }))
                            }
                            errors={errors}
                            cancel={() => setAddSectionVisible(false)}
                            save={handleSaveNewSection}
                        />
                        <i className="fa-solid fa-trash delete-section" onClick={()=>{
                            setAddSectionVisible(false);
                        }}/>
                    </div>
                )}
            </div>
        </div>
    );
}
