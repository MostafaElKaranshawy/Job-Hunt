import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import Section from "../../section/section";
import CustomSection from "../../customSection/customSection";
import Skills from "../../skills/skills";
import "./profileInfoSection.css";
import profileHolder from "../../../assets/profile.png";
import { parseEducation,
    parseExperience,
    toCamelCase,
    parseSection
} from "../../../utils/profileUtils";

import { getUserProfile, 
    editUserProfile,
    handleCustomSectionOperation,
    getUserSkills,
    addSkill,
    removeSkill
} from "../../../services/userProfileService";
export default function ProfileInfoSection() {
    const { userName } = useParams();
    const [userSections, setUserSections] = useState([]);
    const [customSections, setCustomSections] = useState([]);
    const [sections, setSections] = useState(userSections);
    const [userSkills, setUserSkills] = useState([]);
    const[userData, setUserData] = useState({})
    useEffect(() => {
        if(userName){
            getUserData();
        }
    },[userName])
    async function getUserData() {
        const userData = await getUserProfile(userName);
        console.log(userData)
        setUserData(userData)
        setUserSections([
            {
                sectionName: "Personal Info",
                sectionFields: [
                    { fieldName: "First Name", fieldValue: userData.firstName, fieldType: "text"},
                    { fieldName: "Last Name", fieldValue: userData.lastName, fieldType: "text"},
                    { fieldName: "Phone Number", fieldValue: userData.phoneNumber, fieldType: "text"},
                    { fieldName: "Country", fieldValue: userData.country, fieldType: "text"},
                    { fieldName: "State", fieldValue: userData.state, fieldType: "text"},
                    { fieldName: "City", fieldValue: userData.city, fieldType: "text"},
                    { fieldName: "Address", fieldValue: userData.address, fieldType: "text"},
                ],
            },
        ]);
    }
    useEffect(() => {
        if (userData.id) {
            // handleGetUserSkills();
            refreshCustomSections();
        }
    }, [userData]);
    useEffect(()=>{
        if(userSections){
            setSections(userSections)
        }
    },[userSections])

    const handleSectionChange = (sectionName, fieldName, fieldValue) => {
        console.log(sectionName, fieldName, fieldValue)
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
                console.log(section)
                return section;
            })
        );
    };

    // Adding a section
    const handleAddSection = async (parentSectionName, newSection) => {
        const sectionData = newSection.sectionFields.reduce((acc, field) => {
            return {
                ...acc,
                [toCamelCase(field.fieldName)]: field.fieldValue || "", // Use empty string if fieldValue is undefined
            };
        }, {});
        
        console.log(sectionData)
        if (parentSectionName === "Education" || parentSectionName === "Experience") {
            await handleCustomSectionOperation(
                'POST',
                parentSectionName,
                sectionData,
                userData.id
            );
        }
        await refreshCustomSections(); // Refresh data after modification
    };
    
    // Removing a section
    const handleRemoveSection = async (parentSectionName, sectionId) => {
        console.log(parentSectionName, sectionId)
        if (parentSectionName === "Education" || parentSectionName === "Experience") {
            await handleCustomSectionOperation(
                'DELETE',
                parentSectionName,
                null,
                sectionId
            );
        }
        await refreshCustomSections(); // Refresh data after modification
    };
    
    // Editing a section
    const handleEditSection = async (parentSectionName, id, editedSection) => {
        console.log(parentSectionName, id, editedSection)
        const sectionData = editedSection.reduce((acc, field) => {
            return {
                ...acc,
                [toCamelCase(field.fieldName)]: field.fieldValue || "", // Use empty string if fieldValue is undefined
            };
        }, {});
        if (parentSectionName === "Education" || parentSectionName === "Experience") {
            await handleCustomSectionOperation(
                'PUT',
                parentSectionName,
                sectionData,
                id
            );
        }
        await refreshCustomSections(); // Refresh data after modification
    };
    const handleCustomSectionChange = (sectionName, fieldName, fieldValue, sectionType) => {
        setCustomSections(
            customSections.map((section) => {
                if (section.sectionName === sectionName) {
                    return {
                        ...section,
                        sectionSections: section.sectionSections.map((customSection) => {
                            if (customSection.sectionName === sectionName) {
                                return {
                                    ...customSection,
                                    sectionFields: customSection.sectionFields.map((field) => {
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
                            return customSection;
                        }),
                    };
                }
                return section;
            })
        );
    }
    // Refresh sections data
    const refreshCustomSections = async () => {
        // console.log(userData.id)
        const educationData = await handleCustomSectionOperation('GET', 'Education', null, userData.id);
        const experienceData = await handleCustomSectionOperation('GET', 'Experience', null, userData.id);
    
        setCustomSections([
            {   
                sectionName: "Education",
                sectionSections: educationData? educationData.map((education, index)=>{
                    // console.log(education.id)
                    return {
                        id: education.id,
                        sectionName: "Education " + index,
                        sectionFields: parseEducation(education)
                    }
                }) : [],
            },
            {
                sectionName: "Experience",
                sectionSections: experienceData?  experienceData.map((experience, index)=>{
                    return {
                        id: experience.id,
                        sectionName: "Experience " + index,
                        sectionFields: parseExperience(experience)
                    }
                }): [],
            },
        ]);
    };
    
    async function saveChanges(){
        // Personal Information
        const personalInfo = parseSection(
            sections.find(section => section.sectionName === "Personal Info")
        );
        const updatedData = {
            ...personalInfo
        };
    
        console.log("Updated User Data: ", updatedData);
        
        await editUserProfile(userName, updatedData);
        await getUserData(userName)
    } 
    const handleGetUserSkills = async () => {
        const skillsData = await getUserSkills(userName);
        setUserSkills(skillsData)
    }
    const addUserSkill = async (skillid) => {
        await addSkill(userName, skillid);
        await handleGetUserSkills()
    }
    const removeUserSkill = async (skillid) => {
        await removeSkill(userName, skillid);
        await handleGetUserSkills()
    }
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
                                save={saveChanges}
                                cancel={getUserData}
                            />
                        );
                    })}
                {   
                    customSections &&(
                        customSections.map((section, index) => (
                            <CustomSection
                                key={index}
                                sectionData={section}
                                sectionChange={handleCustomSectionChange}
                                editSection={handleEditSection}
                                addSection={handleAddSection}
                                removeSection={handleRemoveSection}
                                refreshCustomSections={refreshCustomSections}
                            />
                        ))
                    )
                }
            </div>
            <div className="skills-section">
                <Skills
                    skills={userSkills}
                    changeUserSkills={setUserSkills}
                    addSkill={addUserSkill}
                    removeSkill={removeUserSkill}
                    />
            </div>
        </div>
    );
}
