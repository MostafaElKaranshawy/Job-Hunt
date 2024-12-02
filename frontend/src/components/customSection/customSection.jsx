import React, { useState } from "react";
import Section from "../section/section";
import "./customSection.css";

export default function CustomSection({ sectionData, sectionChange, addSection, editSection, removeSection, refreshCustomSections }) {
    const [addSectionVisible, setAddSectionVisible] = useState(false);
    const [newSection, setNewSection] = useState({
        sectionName: `${sectionData.sectionName} ${sectionData.sectionSections.length + 1}`,
        sectionFields: sectionData.sectionName === "Education"
            ? [
                { fieldName: "Institution", fieldValue: "", fieldType: "text" },
                { fieldName: "Degree", fieldValue: "" , fieldType: "text"},
                { fieldName: "Field Of Study", fieldValue: "" , fieldType: "text"},
                { fieldName: "Start Date", fieldValue: "" , fieldType: "number"},
                { fieldName: "End Date", fieldValue: "" , fieldType: "number"},
              ]
            : [
                { fieldName: "Title", fieldValue: "", fieldType: "text" },
                { fieldName: "Company", fieldValue: "", fieldType: "text" },
                { fieldName: "Location", fieldValue: "", fieldType: "text" },
                { fieldName: "Start Date", fieldValue: "", fieldType: "date" },
                { fieldName: "End Date", fieldValue: "" , fieldType: "date"},
                { fieldName: "Description", fieldValue: "", fieldType: "text" },
              ]
    });
    const [newSectionErrors, setNewSectionErrors] = useState({});

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
        if(newSection.sectionFields[3].fieldValue > newSection.sectionFields[4].fieldValue){
            errors["End Date"] = "End Date should be greater than Start Date";
        }
        
        if (Object.keys(errors).length > 0) {
            setNewSectionErrors(errors);

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
                    className={`add-section ${addSectionVisible ? "active-add-section" : ""}`}
                    onClick={() => {
                        setAddSectionVisible(!addSectionVisible);
                        setNewSectionErrors({});
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
                            cancel={()=>{refreshCustomSections}}
                            errors={section.sectionErrors}
                        />
                        {/* {console.log(sectionData.sectionErrors)} */}
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
                            errors={newSectionErrors}
                            cancel={() => setAddSectionVisible(false)}
                            save={handleSaveNewSection}
                        />
                    </div>
                )}
            </div>
        </div>
    );
}
