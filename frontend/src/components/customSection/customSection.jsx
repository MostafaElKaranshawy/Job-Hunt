import React, {useState} from "react";

import Section from "../section/section";
import './customSection.css';
export default function CustomSection({ sectionData, sectionChange, addSection }) {
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

    const handleAddSection = () => {
        addSection(sectionData.sectionName, newSection); // Call parent handler
        setAddSectionVisible(false); // Hide add section UI
    };
    const addNewSection = (SectionName, fieldName, fieldValue)=>{
        (sectionName, fieldName, fieldValue) => {
            const updatedSection = {
                ...sectionData,
                sectionSections: sectionData.sectionSections.map(
                    (subSection, subIndex) =>
                        subIndex === index
                            ? {
                                  ...subSection,
                                  sectionFields: subSection.sectionFields.map(
                                      (field) =>
                                          field.fieldName === fieldName
                                              ? { ...field, fieldValue }
                                              : field
                                  ),
                              }
                            : subSection
                ),
            };
            sectionChange(sectionData.sectionName, updatedSection);
        }
    }
    return (
        <div className="custom-section">
            <div className="section-header">
                <div className="section-name">{sectionData.sectionName}</div>
                <div
                    className="add-section"
                    onClick={() => setAddSectionVisible(!addSectionVisible)}
                >
                    +
                </div>
            </div>
            <div className="section-body">
                {sectionData.sectionSections.map((section, index) => (
                    <Section
                        key={index}
                        sectionData={section}
                        sectionChange={addNewSection}
                    />
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
                            }}
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
