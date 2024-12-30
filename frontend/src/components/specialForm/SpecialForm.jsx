import React, { useEffect, useState } from "react";
import EducationSection from "../primarySections/EducationSection";
import ExperienceSection from "../primarySections/ExperienceSection";
import PersonalSection from "../primarySections/PersonalSection.jsx";
import SkillSection from "../primarySections/skillSection";
import SpecialSection from "../specialSection/SpecialSection.jsx";
import SpecialField from "../specialSection/SpecialField.jsx";
import "./specialForm.css";

export default function SpecialForm({open, onClose, sectionData, job }) {
    const [personalData, setPersonalData] = useState({});
    const [educationData, setEducationData] = useState({});
    const [experienceData, setExperienceData] = useState({});
    const [skillData, setSkillData] = useState([]);
    const [specialSectionsData, setSpecialSectionsData] = useState([]);
    const [specialFieldsData, setSpecialFieldsData] = useState([]);
    

    const handleSubmitButton = () => {
        console.log({
            personalData,
            educationData,
            experienceData,
            skillData,
            specialSectionsData,
            specialFieldsData,
        });
        // onClose();
    };

    const handleSpecialSectionChange = (sectionName, data) => {
        setSpecialSectionsData((prevData) => {
            const updatedData = prevData.filter(section => section.sectionName !== sectionName);
            return [...updatedData, { sectionName, data }];
        });
    };

    const handleSpecialFieldChange = (index, data) => {
        setSpecialFieldsData((prevData) => {
            const updatedData = [...prevData];
            updatedData[index] = data;
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
                        onChange={(sectionData) => handleSpecialSectionChange(data.sectionName, sectionData)}
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

                <button type="sumbit" className="form-button" onClick={handleSubmitButton}>Submit</button>
            </form>
            
        </div>
        }
        </>
    );
}