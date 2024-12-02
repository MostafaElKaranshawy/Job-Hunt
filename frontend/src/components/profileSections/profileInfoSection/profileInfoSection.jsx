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
    handleCustomSectionOperation
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
        setUserData(userData)
        setUserSections([
            {
                sectionName: "Personal Info",
                sectionFields: [
                    { fieldName: "First Name", fieldValue: userData.firstName, fieldType: "text"},
                    { fieldName: "Last Name", fieldValue: userData.lastName, fieldType: "text"},
                    { fieldName: "Email", fieldValue: userData.email, fieldType: "text"},
                    { fieldName: "Phone Number", fieldValue: userData.phoneNumber, fieldType: "text"},
                    { fieldName: "Country", fieldValue: userData.country, fieldType: "text"},
                    { fieldName: "State", fieldValue: userData.state, fieldType: "text"},
                    { fieldName: "City", fieldValue: userData.city, fieldType: "text"},
                    { fieldName: "Address", fieldValue: userData.address, fieldType: "text"},
                ],
            },
        ]);
        
        setUserSkills(userData.skills);
        
        setCustomSections([
            {
                sectionName: "Education",
                sectionSections: userData.educationList.map((education, index) => ({
                    id: education.id,
                    sectionName: `Education ${index + 1}`,
                    sectionFields: parseEducation(education)
                })),
            },
            {
                sectionName: "Experience",
                sectionSections: userData.experienceList.map((experience, index) => ({
                    sectionName: `Experience ${index + 1}`,
                    sectionFields: parseExperience(experience)
                })),
            },
        ]);
        
        console.log(userData)
    }
    useEffect(()=>{
        if(userSections){
            setSections(userSections)
        }
    },[userSections])

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

    // Adding a section
    const handleAddSection = async (parentSectionName, newSection) => {
        if (parentSectionName === "Education" || parentSectionName === "Experience") {
            await handleCustomSectionOperation(
                'POST',
                parentSectionName,
                userName,
                newSection
            );
        }
        await refreshCustomSections(); // Refresh data after modification
    };
    
    // Removing a section
    const handleRemoveSection = async (parentSectionName, sectionName) => {
        setCustomSections(
            customSections.map((section) => {
                if (section.sectionName === parentSectionName) {
                    return {
                        ...section,
                        sectionSections: section.sectionSections.filter(
                            (subSection) => subSection.sectionName !== sectionName
                        ),
                    };
                }
                return section;
            })
        );
        await refreshCustomSections(); // Refresh data after modification
    };
    
    // Editing a section
    const handleEditSection = async (parentSectionName, editedSection) => {
        if (parentSectionName === "Education" || parentSectionName === "Experience") {
            await handleCustomSectionOperation(
                'PUT',
                parentSectionName,
                userName,
                editedSection
            );
        }
        await refreshCustomSections(); // Refresh data after modification
    };
    
    // Refresh sections data
    const refreshCustomSections = async () => {
        const educationData = await handleCustomSectionOperation('GET', 'Education', userName);
        const experienceData = await handleCustomSectionOperation('GET', 'Experience', userName);
    
        setCustomSections([
            {
                sectionName: "Education",
                sectionSections: educationData || [],
            },
            {
                sectionName: "Experience",
                sectionSections: experienceData || [],
            },
        ]);
    };
    
    // Initial data fetch
    useEffect(() => {
        refreshCustomSections();
    }, []);
    async function saveChanges(){
        // Personal Information
        const personalInfo = parseSection(
            sections.find(section => section.sectionName === "Personal Info")
        );
        
        // Education List
        const educationList = customSections
            .find(section => section.sectionName === "Education")
            ?.sectionSections.map(parseSection);
        
        // Experience List
        const experienceList = customSections
            .find(section => section.sectionName === "Experience")
            ?.sectionSections.map(parseSection);
        
    
        console.log(educationList)
    
        const updatedData = {
            ...personalInfo,
            // skills: userSkills,
            // educationList: educationList || [],
            // experienceList: experienceList || [],
        };
    
        console.log("Updated User Data: ", updatedData);
        // Send this updatedData to your backend via editUserProfile or any other service function
        await editUserProfile(userName, updatedData);
        // await getUserData(userName)
    } 
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
                                sectionChange={handleEditSection}
                                addSection={handleAddSection}
                                removeSection={handleRemoveSection}
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
        </div>
    );
}
