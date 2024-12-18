import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import Section from "../../section/section";
import ProfileSection from "../../profileSection/profileSection";
import CustomSection from "../../customSection/customSection";
import Skills from "../../skills/skills";
import "./profileInfoSection.css";
import profileHolder from "../../../assets/profile.png";
import { parseEducation,
    parseExperience,
    toCamelCase,
} from "../../../utils/profileUtils";

import { getUserProfile, 
    editUserProfile,
    handleCustomSectionOperation,
} from "../../../services/userProfileService";


export default function ProfileInfoSection() {
    const { userName } = useParams();
    const [render, setRender] = useState(false);
    const [userSections, setUserSections] = useState([]);
    const [customSections, setCustomSections] = useState([]);
    const [sections, setSections] = useState(userSections);
    const [userData, setUserData] = useState({})

    useEffect(() => {
        if (userName) {
            getUserData();
        }
    },[userName])

    useEffect(() => {
        if (userData.id) {
            refreshCustomSections();
        }
    }, [userData]);
    
    useEffect(()=>{
        if (userSections) {
            setSections(userSections)
        }
    },[userSections])


    async function getUserData() {
        const userData = await getUserProfile(userName);
        console.log(userData)
        setUserData(userData)
        setUserSections([
            {
                sectionName: "Personal Info",
                sectionFields: [
                    { fieldName: "First Name", fieldValue: userData.firstName, fieldType: "text", minLength: 5, maxLength: 20 },
                    { fieldName: "Last Name", fieldValue: userData.lastName, fieldType: "text", minLength: 5, maxLength: 20 },
                    { fieldName: "Phone Number", fieldValue: userData.phoneNumber, fieldType: "text", minLength: 10, maxLength: 20 },
                    { fieldName: "Country", fieldValue: userData.country, fieldType: "select", minLength: 0, maxLength: 30 },
                    { fieldName: "State", fieldValue: userData.state, fieldType: "text", minLength: 5, maxLength: 20 },
                    { fieldName: "City", fieldValue: userData.city, fieldType: "text", minLength: 5, maxLength: 20 },
                    { fieldName: "Address", fieldValue: userData.address, fieldType: "text", minLength: 5, maxLength: 50 },
                ],
                errors: {},
            },
        ]);
        setRender(!render);
    }

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
                        errors: {},
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

    const handleEditSection = async (parentSectionName, id, editedSection) => {

        const errors = {};
        const sectionData = {};
    
        // Loop through the editedSection to validate fields
        editedSection.forEach((field) => {
            // Check if the field is empty
            if (!field.fieldValue || field.fieldValue == '') {
                if(field.fieldName != 'End Date')
                    errors[field.fieldName] = "This field is required.";
            } 
            else if(field.fieldValue.length < field.minLength || field.fieldValue.length > field.maxLength){
                errors[field.fieldName] = `${field.fieldName} should be between ${field.minLength} and ${field.maxLength} characters.`;
            }
            else {
                sectionData[toCamelCase(field.fieldName)] = field.fieldValue || "";
            }
        });
    
        // Validate start and end dates if applicable
        const startDate = editedSection.find(field => field.fieldName === "Start Date")?.fieldValue;
        const endDate = editedSection.find(field => field.fieldName === "End Date")?.fieldValue;
    
        if(startDate < 2000){
            errors["Start Date"] = "Year must be above 2000";
        }
        if(endDate != '' && endDate < 2000){
            errors["End Date"] = "Year must be above 2000";
        }
        if (startDate> new Date().getFullYear()){
            errors["Start Date"] = "Start Date should be before current date.";
        }
        if (startDate && endDate) {
            if (endDate < startDate) {
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
                                    errors: {}
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
                errors: {},
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
                errors: {},
            },
        ]);
        setRender(!render);
    };
    
    async function saveChanges() {
        // Find the Personal Info section
        const personalInfo = sections.find((section) => section.sectionName === "Personal Info");
        if (!personalInfo) {
            return;
        }
    
        const errors = {};
    
        // Validate each field in Personal Info
        personalInfo.sectionFields.forEach((field) => {
            if(field.fieldValue.length > 0 && field.fieldValue.length < field.minLength || field.fieldValue.length > field.maxLength){
                errors[field.fieldName] = `${field.fieldName} should be between ${field.minLength} and ${field.maxLength} characters.`;
            }
            if(field.fieldName == 'First Name'){
                if (!field.fieldValue || field.fieldValue.trim() === "") {
                    errors[field.fieldName] = `${field.fieldName} is required.`;
                }
            }
        });
    
        // Update sections with errors
        setSections(
            sections.map((section) => {
                if (section.sectionName === "Personal Info") {
                    return {
                        ...section,
                        errors,
                    };
                }
                return section;
            })
        );
    
        // If there are errors, stop and log them
        if (Object.keys(errors).length > 0) {
            return;
        }
    
        // Construct updated data only if there are no errors
        const updatedData = personalInfo.sectionFields.reduce((acc, field) => {
            acc[toCamelCase(field.fieldName)] = field.fieldValue.trim();
            return acc;
        }, {});
    
        console.log("Updated User Data: ", updatedData);
    
        await editUserProfile(userName, updatedData);
        await getUserData(userName);
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
                            <ProfileSection
                                sectionData={section}
                                sectionChange={handleSectionChange}
                                key={index}
                                errors={section.errors}
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
            {/* <div className="skills-section">
                <Skills username={userName}/>
            </div> */}
        </div>
    );
}
