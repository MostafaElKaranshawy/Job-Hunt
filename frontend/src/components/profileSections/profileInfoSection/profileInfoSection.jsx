import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import Section from "../../section/section";
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
        },
        {
            sectionName: "Education",
            sectionFields: [
                {
                    fieldName: "School",
                    fieldValue: "Cairo University",
                },
                {
                    fieldName: "Major",
                    fieldValue: "Computer Science",
                },
                {
                    fieldName: "Graduation Year",
                    fieldValue: "2020",
                },
            ],
        },
        {
            sectionName: "Experience",
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
        },
    ]);
    const [sections, setSections] = useState(userSections);
    const [userSkills, setUserSkills] = useState(["python"]);

    // const changeUserSkills = (skill) => {
    //     setUserSkills([...userSkills, skill]);
    //     console.log(skill);
    // };

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

    return (
        <div className="profile-info-section">
            <div className="profile-picture">
                <img src={profileHolder} />
            </div>
            <div className="profile-data-sections">
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
            </div>
            <div className="skills-section">
                <Skills
                    skills={userSkills}
                    changeUserSkills={setUserSkills}
                />
            </div>
        </div>
    );
}
