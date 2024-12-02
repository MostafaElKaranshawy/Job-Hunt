import React, { useState } from "react";
import Section from "../section/section";
import "./customSection.css";

export default function CustomSection({ sectionData, sectionChange, addSection, removeSection }) {
    const [addSectionVisible, setAddSectionVisible] = useState(false);
    const [editSectionVisible, setEditSectionVisible] = useState(null);
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

    const handleEditSection = (section) => {
        setEditSectionVisible(section.sectionName);
    };

    const handleSaveEditedSection = (sectionName, editedFields) => {
        const updatedSections = sectionData.sectionSections.map((subSection) => {
            if (subSection.sectionName === sectionName) {
                return {
                    ...subSection,
                    sectionFields: editedFields,
                };
            }
            return subSection;
        });
        sectionChange(sectionData.sectionName, { ...sectionData, sectionSections: updatedSections });
        setEditSectionVisible(null);
    };

    const handleCancelEditSection = () => {
        setEditSectionVisible(null);  // Simply hide the edit mode
    };

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
                        {editSectionVisible === section.sectionName ? (
                            <Section
                                sectionData={section}
                                sectionChange={(fieldName, fieldValue) =>
                                    handleFieldChange(section.sectionName, fieldName, fieldValue)
                                }
                                save={(editedFields) => handleSaveEditedSection(section.sectionName, editedFields)}
                                cancel={handleCancelEditSection}
                            />
                        ) : (
                            <Section
                                sectionData={section}
                                sectionChange={handleFieldChange}
                            />
                        )}
                        <div className="actions">
                            {editSectionVisible === section.sectionName ? (
                                <button onClick={() => handleSaveEditedSection(section.sectionName, section.sectionFields)}>
                                    Save
                                </button>
                            ) : (
                                <button onClick={() => handleEditSection(section)}>Edit</button>
                            )}
                            <button onClick={() => removeSection(sectionData.sectionName, section.sectionName)}>
                                Delete
                            </button>
                        </div>
                    </div>
                ))}
                {addSectionVisible && (
                    <div className="section-added">
                        <Section
                            sectionData={newSection}
                            sectionChange={(fieldName, fieldValue) =>
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
                            save={() => addSection(sectionData.sectionName, newSection)}
                        />
                    </div>
                )}
            </div>
        </div>
    );
}
