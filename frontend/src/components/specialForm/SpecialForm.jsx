import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import EducationSection from "../primarySections/EducationSection";
import ExperienceSection from "../primarySections/ExperienceSection";
import PersonalSection from "../primarySections/PersonalSection.jsx";
import SkillSection from "../primarySections/skillSection";
import SpecialSection from "../specialSection/SpecialSection.jsx";
import SpecialField from "../specialSection/SpecialField.jsx";
import "./specialForm.css";

export default function SpecialForm({open, onClose, sectionData, job }) {
    const { userName } = useParams();
    const [personalData, setPersonalData] = useState(null);
    const [educationData, setEducationData] = useState(null);
    const [experienceData, setExperienceData] = useState(null);
    const [skillData, setSkillData] = useState([]);
    const [specialSectionsData, setSpecialSectionsData] = useState([]);
    const [specialFieldsData, setSpecialFieldsData] = useState([]);
    const [currentJob, setCurrentJob] = useState({});
    const [isSubmitted, setIsSubmitted] = useState(false);
    
    useEffect(() => {
        setCurrentJob(job);
    },[job])

    useEffect(() => {
        if (isSubmitted) {
            setPersonalData(null);
            setEducationData(null);
            setExperienceData(null);
            setSkillData([]);
            setSpecialSectionsData([]);
            setSpecialFieldsData([]);
            setIsSubmitted(false); // Reset the submission status
        }
    }, [isSubmitted]);
    
    async function sendResponse(userName, id, data, callback) {
        userName = "jesselingard253";
        try{
            const url = `http://localhost:8080/job/${userName}/${id}/form/response`;
            const response = await fetch(url, {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            });

            if (callback) callback();

    
            if (!response.ok) {
                throw new Error(`Response status: ${response.status}`);
            }
    
            const res = await response.json();
            console.log("Application saved successfully", res);
            return res;
        } catch (error) {
            console.error(error.message);
        }
    }

    const handleSubmitButton = () => {
        const data = {
            personalData,
            educationData,
            experienceData,
            skillData,
            specialSectionsData,
            specialFieldsData,
        }
        console.log(data);
        //sendResponse(userName, currentJob.id,data);
        sendResponse(userName, currentJob.id, data, () => {
            setIsSubmitted(true);
        });
        onClose();
    };

    const handleSpecialSectionChange = (sectionName, data) => {
        setSpecialSectionsData((prevData) => {
            const updatedData = prevData.map((section) =>
                section.sectionName === sectionName ? { sectionName, data } : section
            );
            // Check if the sectionName doesn't already exist in the state
            const isNewSection = !prevData.some(section => section.sectionName === sectionName);
            if (isNewSection) {
                updatedData.push({ sectionName, data });
            }
            return updatedData;
        });
    };

    const handleSpecialFieldChange = (index, value) => {
        setSpecialFieldsData((prevData) => {
            const updatedData = [...prevData];
            // Update the specific field object with both fieldName and value
            updatedData[index] = {
                fieldName: sectionData.fields[index].label, // Or field.name if it exists
                data: value,
            };
            return updatedData;
        });
    };
    

    return (
        <>
        {open && <div className="special-apply-user">
            <button className="close-button" onClick={onClose}>
                        &times;
            </button>
            <h1 style={{ textAlign: "center" }}>Form</h1>
            <form>
                {sectionData?.staticSections?.includes("Personal Information") && (<PersonalSection onChange={setPersonalData} />)}
                {sectionData?.staticSections?.includes("Education") && (<EducationSection onChange={setEducationData} />)}
                {sectionData?.staticSections?.includes("Experience") && (<ExperienceSection onChange={setExperienceData} />)}
                {sectionData?.staticSections?.includes("Skills") && (<SkillSection onChange={setSkillData} />)}


                {sectionData?.sections?.map((data, index) => (
                    <SpecialSection
                        key={index}
                        sectionName={data.name}
                        labels={data.label}
                        fieldType={data.type}
                        fieldOptions={data.options}
                        Mandatory={data.isRequired}
                        onChange={(sectionData) => handleSpecialSectionChange(data.name, sectionData)}
                    />
                ))}

                {sectionData.fields?.map((field, index) => (
                    <SpecialField
                        key={index}
                        label={field.label}
                        fieldType={field.type}
                        fieldOptions={field.options}
                        isMandatory={field.isRequired}
                        onChange={(fieldData) => handleSpecialFieldChange(index, fieldData)}
                    />
                ))}

                <button type="submit" className="form-button" onClick={handleSubmitButton}>Submit</button>
            </form>
            
        </div>
        }
        </>
    );
}