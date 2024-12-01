import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import Section from "../../section/section";
import CustomSection from "../../customSection/customSection";
import Skills from "../../skills/skills";
import "./profileInfoSection.css";
import profileHolder from "../../../assets/profile.png";

export default function ProfileInfoSection() {
    const { userName } = useParams();
    const [userSections, setUserSections] = useState([
        {
            sectionName: "Personal Info",
            sectionFields: [
                {
                    fieldName: "Name",
                    fieldValue: "Mustafa Elkaranshawy",
                },
                {
                    fieldName: "Email",
                    fieldValue: "mostafaelkaranshawy0@gmail.com",
                },
            ],
        }
    ]);
    const [customSections, setCustomSections] = useState([
        {
            sectionName: "Education",
            sectionSections: [
                {   
                    sectionName: "Education 1",
                    sectionFields: [
                        {
                            fieldName: "School",
                            fieldValue: "MIT",
                        },
                        {
                            fieldName: "Major",
                            fieldValue: "Software Engineering",
                        },
                        {
                            fieldName: "Start Date",
                            fieldValue: "2020",
                        },
                        {
                            fieldName: "End Date",
                            fieldValue: "2021",
                        },
                    ],
                },
                {   
                    sectionName: "Education 2",
                    sectionFields: [
                        {
                            fieldName: "School",
                            fieldValue: "MIT",
                        },
                        {
                            fieldName: "Major",
                            fieldValue: "Software Engineering",
                        },
                        {
                            fieldName: "Start Date",
                            fieldValue: "2020",
                        },
                        {
                            fieldName: "End Date",
                            fieldValue: "2021",
                        },
                    ],
                },
            ]
        },
        {
            sectionName: "Experience",
            sectionSections: [
            {   
                sectionName: "Experience 1",
                sectionFields: [
                    {
                        fieldName: "Company",
                        fieldValue: "Google",
                    },
                    {
                        fieldName: "Position",
                        fieldValue: "Software Engineer",
                    },
                    {
                        fieldName: "Start Date",
                        fieldValue: "2020",
                    },
                    {
                        fieldName: "End Date",
                        fieldValue: "2021",
                    },
                ],
                }
            ]
        },
    ]);
    const [sections, setSections] = useState(userSections);
    const [userSkills, setUserSkills] = useState(["python"]);
    const handleCustomSectionChange = (sectionName, updatedSection) => {
        setCustomSections(
            customSections.map((section) => {
                if (section.sectionName === sectionName) {
                    return updatedSection;
                }
                return section;
            })
        );
    };
    
    const handleAddSection = (parentSectionName, newSection) => {
        setCustomSections(
            customSections.map((section) => {
                if (section.sectionName === parentSectionName) {
                    return {
                        ...section,
                        sectionSections: [...section.sectionSections, newSection],
                    };
                }
                return section;
            })
        );
    };
    const handleSectionChange = (sectionName, fieldName, fieldValue) => {
        setSections(
            sections.map((section) => {
                if (section.sectionName === sectionName) {
                    return {
                        ...section,
                        sectionFields: section.sectionFields.map((field) => {
                            if (field.fieldName === fieldName) {
                                return {
                                    ...field,
                                    fieldValue: fieldValue,
                                };
                            }
                            return field;
                        }),
                    };
                }
                return section;
            })
        );
    };
    const saveChanges = () => {} 
    return (
        <div className="profile-info-section">
            <div className="profile-picture">
                <img src={profileHolder} />
            </div>
            <div className="profile-data-sections">
                {/* {
                    sections && (
                        <Section
                            sectionData={sections[0]}
                            sectionChange={handleSectionChange}
                        />
                    )
                } */}
                {sections &&
                    sections.map((section, index) => {
                        return (
                            <Section
                                sectionData={section}
                                sectionChange={handleSectionChange}
                                key={index}
                            />
                        );
                    })}
                {
                    customSections && (
                        customSections.map((section, index) => (
                            <CustomSection
                                key={index}
                                sectionData={section}
                                sectionChange={handleCustomSectionChange}
                                addSection={handleAddSection}
                            />
                        ))
                    )
                }
            </div>
            <div className="skills-section">
                <Skills
                    skills={userSkills}
                    changeUserSkills={setUserSkills}
                />
            </div>
            <div className="save-button" onClick={saveChanges}>
                Save
            </div>
        </div>
    );
}
