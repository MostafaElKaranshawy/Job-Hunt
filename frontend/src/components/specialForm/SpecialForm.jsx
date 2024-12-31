import React, { useEffect, useState } from "react";
import EducationSection from "../primarySections/EducationSection";
import ExperienceSection from "../primarySections/ExperienceSection";
import PersonalSection from "../primarySections/PersonalSection.jsx";
import SkillSection from "../primarySections/skillSection";
import SpecialSection from "../specialSection/SpecialSection.jsx";
import SpecialField from "../specialSection/SpecialField.jsx";
import { useParams } from "react-router-dom";
import "./specialForm.css";

const initialFormState = {
  personalData: null,
  educationData: null,
  experienceData: null,
  skillData: [],
  specialSectionsData: [],
  specialFieldsData: [],
};

export default function SpecialForm({ open, onClose, sectionData, job }) {
  const [personalData, setPersonalData] = useState(initialFormState.personalData);
  const [educationData, setEducationData] = useState(initialFormState.educationData);
  const [experienceData, setExperienceData] = useState(initialFormState.experienceData);
  const [skillData, setSkillData] = useState(initialFormState.skillData);
  const [specialSectionsData, setSpecialSectionsData] = useState(initialFormState.specialSectionsData);
  const [specialFieldsData, setSpecialFieldsData] = useState(initialFormState.specialFieldsData);
  const [currentJob, setCurrentJob] = useState({});
  const userName = document.cookie.split("username=")[1];
  
  // Reset form when sectionData changes or form is closed
  useEffect(() => {
    resetForm();
  }, [sectionData, open]);

  useEffect(() => {
    setCurrentJob(job);
  }, [job]);

  const resetForm = () => {
    setPersonalData(initialFormState.personalData);
    setEducationData(initialFormState.educationData);
    setExperienceData(initialFormState.experienceData);
    setSkillData(initialFormState.skillData);
    setSpecialSectionsData(initialFormState.specialSectionsData);
    setSpecialFieldsData(initialFormState.specialFieldsData);
  };

  async function sendResponse(id, data) {
    try {
      const url = `http://localhost:8080/job/${userName}/${id}/form/response`;
      const response = await fetch(url, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
      });

      if (!response.ok) {
        throw new Error(`Response status: ${response.status}`);
      }

      const res = await response.json();
      return res;
    } catch (error) {
      console.error(error.message);
    }
  }

  const handleSubmitButton = async () => {
    const data = {
      personalData,
      educationData,
      experienceData,
      skillData,
      specialSectionsData,
      specialFieldsData,
    };
    console.log(data);
    try {
      await sendResponse(currentJob.id, data);
      resetForm();
      onClose();
    } catch (error) {
      console.error('Error submitting form:', error);
    }
  };

  const handleSpecialSectionChange = (sectionName, data, type, labels) => {
    setSpecialSectionsData((prevData) => {
      const existingIndex = prevData.findIndex(section => section.sectionName === sectionName);
  
      if (existingIndex !== -1) {
        // Update existing section
        const updatedData = [...prevData];
        const idx = type.indexOf("checkbox");
        if (idx !== -1) {
          const temp = data[labels[idx]];
          let array;
          if (Array.isArray(temp)) {
            array = temp;
          } else if (typeof temp === "string") {
            array = temp.slice(1, -1).split(",").map(item => item.trim());
          } else {
            array = [];
          }
          data[labels[idx]] = array; // Keep as array for frontend
          updatedData[existingIndex] = { sectionName, data: { ...data, [labels[idx]]: "[" + array.join(", ") + "]" } }; // Convert to string for backend
        } else {
          updatedData[existingIndex] = { sectionName, data };
        }
        return updatedData;
      } else {
        // Add new section
        const idx = type.indexOf("checkbox");
        if (idx !== -1) {
          const temp = data[labels[idx]];
          let array;
          if (Array.isArray(temp)) {
            array = temp;
          } else if (typeof temp === "string") {
            array = temp.slice(1, -1).split(",").map(item => item.trim());
          } else {
            array = [];
          }
          data[labels[idx]] = array; // Keep as array for frontend
          return [...prevData, { sectionName, data: { ...data, [labels[idx]]: "[" + array.join(", ") + "]" } }]; // Convert to string for backend
        }
        return [...prevData, { sectionName, data }];
      }
    });
  };

  const handleSpecialFieldChange = (index, value, name, f) => {
    setSpecialFieldsData((prevData) => {
      const existingIndex = prevData.findIndex(field => field.fieldName === name);
      
      if (existingIndex !== -1) {
        // Update existing field
        const updatedData = [...prevData];
        if(f.type === "checkbox") {
            updatedData[existingIndex] = { fieldName: name, data: Array.isArray(value) ? "[" + value.join(", ") + "]" : value };
          return updatedData;
        }
        updatedData[existingIndex] = { fieldName: name, data: value };
        return updatedData;
      } else {
        if(f.type === "checkbox") {
          return [...prevData, { fieldName: name, data: Array.isArray(value) ? "[" + value.join(", ") + "]" : value }];
      }
        // Add new field
        return [...prevData, { fieldName: name, data: value }];
      }
    });
  };

  return (
    <>
      {open && (
        <div className="special-apply-user">
          <button className="close-button" onClick={onClose}>
            &times;
          </button>
          <h1 style={{ textAlign: "center" }}>Form</h1>
          <form onSubmit={(e) => e.preventDefault()}>
            {sectionData?.staticSections?.includes("Personal Information") && (
              <PersonalSection onChange={setPersonalData} value={personalData} />
            )}
            {sectionData?.staticSections?.includes("Education") && (
              <EducationSection onChange={setEducationData} value={educationData} />
            )}
            {sectionData?.staticSections?.includes("Experience") && (
              <ExperienceSection onChange={setExperienceData} value={experienceData} />
            )}
            {sectionData?.staticSections?.includes("Skills") && (
              <SkillSection onChange={setSkillData} value={skillData} />
            )}

            {sectionData?.sections?.map((data, index) => (
              <SpecialSection
                key={`${data.name}-${index}`}
                sectionName={data.name}
                labels={data.label}
                fieldType={data.type}
                fieldOptions={data.options}
                Mandatory={data.isRequired}
                onChange={(sectionData) => handleSpecialSectionChange(data.name, sectionData, data.type, data.label)}
              />
            ))}

            {sectionData.fields?.map((field, index) => (
              <SpecialField
                key={`${field.label}-${index}`}
                label={field.label}
                fieldType={field.type}
                fieldOptions={field.options}
                isMandatory={field.isRequired}
                onChange={(fieldData) => handleSpecialFieldChange(index, fieldData, field.label, field)}
              />
            ))}

            <button type="button" className="form-button" onClick={handleSubmitButton}>
              Submit
            </button>
          </form>
        </div>
      )}
    </>
  );
}