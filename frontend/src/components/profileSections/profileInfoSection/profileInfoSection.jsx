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
    // const handleEditSection = async (parentSectionName, id, editedSection) => {
    //     console.log(parentSectionName, id, editedSection)
    //     const sectionData = editedSection.reduce((acc, field) => {
    //         return {
    //             ...acc,
    //             [toCamelCase(field.fieldName)]: field.fieldValue || "", // Use empty string if fieldValue is undefined
    //         };
    //     }, {});
    //     if (parentSectionName === "Education" || parentSectionName === "Experience") {
    //         await handleCustomSectionOperation(
    //             'PUT',
    //             parentSectionName,
    //             sectionData,
    //             id
    //         );
    //     }
    //     await refreshCustomSections(); // Refresh data after modification
    // };
    const handleEditSection = async (parentSectionName, id, editedSection) => {
        console.log(parentSectionName, id, editedSection);
    
        const errors = {};
        const sectionData = {};
    
        // Loop through the editedSection to validate fields
        editedSection.forEach((field) => {
            // Check if the field is empty
            if (!field.fieldValue) {
                errors[field.fieldName] = "This field is required.";
            } else {
                sectionData[toCamelCase(field.fieldName)] = field.fieldValue || "";
            }
        });
    
        // Validate start and end dates if applicable
        const startDate = editedSection.find(field => field.fieldName === "Start Date")?.fieldValue;
        const endDate = editedSection.find(field => field.fieldName === "End Date")?.fieldValue;
    
        if (startDate && endDate) {
            // Ensure End Date is after Start Date
            if (new Date(endDate) <= new Date(startDate)) {
                errors["End Date"] = "End Date should be after Start Date.";
            }
        }
        setCustomSections([
            ...customSections.map((section) => {
                if (section.sectionName === parentSectionName) {
                    return {
                        ...section,
                        sectionSections: section.sectionSections.map((customSection) => {
                            if (customSection.id === id) {
                                return {
                                    ...customSection,
                                    sectionFields: editedSection,
                                    sectionErrors: errors,
                                };
                            }
                            return customSection;
                        }),
                    };
                }
                return section;
            }),
        ])
        if (Object.keys(errors).length > 0) {
            // If there are errors, return and don't proceed with the update
            console.log("Validation errors: ", errors);
            return;
        }
    
        // If no errors, proceed with the PUT request to update the section
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
        const educationData = await handleCustomSectionOperation('GET', 'Education', null, userData.id);
        const experienceData = await handleCustomSectionOperation('GET', 'Experience', null, userData.id);
    
        setCustomSections([
            {   
                sectionName: "Education",
                sectionSections: educationData? educationData.map((education, index)=>{
                    return {
                        id: education.id,
                        sectionName: "Education " + (index+1),
                        sectionFields: parseEducation(education),
                        sectionErrors:[]
                    }
                }) : [],
            },
            {
                sectionName: "Experience",
                sectionSections: experienceData?  experienceData.map((experience, index)=>{
                    return {
                        id: experience.id,
                        sectionName: "Experience " + (index+1),
                        sectionFields: parseExperience(experience),
                        sectionErrors:[]
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
                <Skills />
            </div>
        </div>
    );
}
