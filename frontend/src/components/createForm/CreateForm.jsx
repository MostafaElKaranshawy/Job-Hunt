import React, { useState } from "react";
import EducationSection from "../primarySections/EducationSection";
import ExperienceSection from "../primarySections/ExperienceSection";
import PersonalSection from "../primarySections/PersonalSection.jsx";
import SkillSection from "../primarySections/skillSection";
import SpecialSection from "../specialSection/SpecialSection.jsx";
import SpecialField from "../specialSection/SpecialField.jsx";
import CreateJob from '../../components/CreateJob/createJob';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import "./specialForm.css";

export default function CreateForm({jobDetails,whenSave}) {
    const [personalData, setPersonalData] = useState({});
    const [educationData, setEducationData] = useState({});
    const [experienceData, setExperienceData] = useState({});
    const [skillData, setSkillData] = useState([]);
    const [specialSections, setSpecialSections] = useState([]);
    const [isCreatingSection, setIsCreatingSection] = useState(false);
    const [currentSection, setCurrentSection] = useState({
        sectionName: "",
        labels: [""],
        fieldType: ["text"],
        fieldOptions: [[]],
        Mandatory: [false]
    });
    
    const [standaloneFields, setStandaloneFields] = useState([]);
    const [isCreatingField, setIsCreatingField] = useState(false);
    const [currentField, setCurrentField] = useState({
        label: "",
        type: "text",
        options: [],
        isRequired: false
    });

    const [trackedSections, setTrackedSections] = useState([]);

    const [showCreateJob, setShowCreateJob] = useState(false);

    const mainSections = ["Personal Information", "Education", "Experience", "Skills"];
    const { companyUsername } = useParams();

    const handleLogData = () => {
        const formattedSections = specialSections.map(section => ({
            name: section.sectionName,
            label: section.labels,
            type: section.fieldType,
            options: section.fieldOptions,
            isRequired: section.Mandatory
        }));
    
        const formattedStandaloneFields = standaloneFields.map(field => ({
            label: field.label,
            type: field.type,
            options: field.options,
            isRequired: field.isRequired
        }));
    
        console.log({
            title: jobDetails.title,
            description: jobDetails.description,
            category: jobDetails.category,
            location: jobDetails.location,
            workLocation: jobDetails.workLocation,
            level: jobDetails.level,
            employmentType: jobDetails.employmentType,
            applicationDeadline: jobDetails.deadline,
            salary:jobDetails.salary,

            sections:formattedSections,
            staticSections:trackedSections,
            fields: formattedStandaloneFields
        });
    
        // const companyUsername = "temp name";
        const url = `http://localhost:8080/company/${companyUsername}/jobs/create`;
    
        axios.post(url, { 
            title: jobDetails.title,
            description: jobDetails.description,
            category: jobDetails.category,
            location: jobDetails.location,
            workLocation: jobDetails.workLocation,
            level: jobDetails.level,
            employmentType: jobDetails.employmentType,
            applicationDeadline: jobDetails.deadline,
            salary:jobDetails.salary,

            sections:formattedSections,
            staticSections:trackedSections,
            fields: formattedStandaloneFields,
            
            },{
                withCredentials: true
            }
        )
            .then(response => {
                console.log('Data successfully sent to the backend:', response.data);
            })
            .catch(error => {
                console.error('Error sending data to the backend:', error);
            });
            whenSave();
            setShowCreateJob(false);
    };

    const addSection = (sectionName) => {
        setTrackedSections((prevSections) => [...prevSections, sectionName]);
    };
    
    const removeSectionByName = (sectionName) => {
        setTrackedSections((prevSections) => prevSections.filter(name => name !== sectionName));
    };

    const startCreatingSection = () => {
        setIsCreatingSection(true);
        setCurrentSection({
            sectionName: "",
            labels: [""],
            fieldType: ["text"],
            fieldOptions: [[]],
            Mandatory: [false]
        });
    };

    const addField = () => {
        if (currentSection.labels.length < 10) {
            setCurrentSection((prevSection) => ({
                ...prevSection,
                labels: [...prevSection.labels, ""],
                fieldType: [...prevSection.fieldType, "text"],
                fieldOptions: [...prevSection.fieldOptions, []],
                Mandatory: [...prevSection.Mandatory, false]
            }));
        } else {
            alert("You can add up to 10 fields per section only.");
        }
    };

    const removeField = (fieldIndex) => {
        if (currentSection.labels.length > 1) {
            setCurrentSection((prevSection) => {
                const newLabels = [...prevSection.labels];
                const newFieldType = [...prevSection.fieldType];
                const newFieldOptions = [...prevSection.fieldOptions];
                const newMandatory = [...prevSection.Mandatory];

                newLabels.splice(fieldIndex, 1);
                newFieldType.splice(fieldIndex, 1);
                newFieldOptions.splice(fieldIndex, 1);
                newMandatory.splice(fieldIndex, 1);

                return {
                    ...prevSection,
                    labels: newLabels,
                    fieldType: newFieldType,
                    fieldOptions: newFieldOptions,
                    Mandatory: newMandatory
                };
            });
        } else {
            alert("Each section must have at least one field.");
        }
    };

    const handleFieldChange = (fieldIndex, key, value) => {
        setCurrentSection((prevSection) => {
            const newField = [...prevSection[key]];
            newField[fieldIndex] = value;
            return {
                ...prevSection,
                [key]: newField
            };
        });
    };

    const handleOptionsChange = (fieldIndex, options) => {
        setCurrentSection((prevSection) => {
            const newFieldOptions = [...prevSection.fieldOptions];
            newFieldOptions[fieldIndex] = options;
            return {
                ...prevSection,
                fieldOptions: newFieldOptions
            };
        });
    };

    const finishSection = () => {
        const normalizedSectionName = currentSection.sectionName.trim().toLowerCase();
        const existingSectionNames = specialSections.map(section => section.sectionName.toLowerCase());

        if (currentSection.sectionName.trim() === "") {
            alert("Section name cannot be empty.");
            return;
        }
        if (currentSection.sectionName.length > 50) {
            alert("Section name cannot exceed 50 characters.");
            return;
        }
        if (mainSections.map(name => name.toLowerCase()).includes(normalizedSectionName) || existingSectionNames.includes(normalizedSectionName)) {
            alert(`Section name cannot be one of the main sections or duplicate an existing section: ${mainSections.join(", ")}`);
            return;
        }
        if (currentSection.labels.some(label => label.trim() === "")) {
            alert("All field labels must be filled.");
            return;
        }
        if (currentSection.labels.some(label => label.length > 50)) {
            alert("Field labels cannot exceed 50 characters.");
            return;
        }

        setSpecialSections([...specialSections, currentSection]);
        setIsCreatingSection(false);
    };

    const removeSection = (index) => {
        setSpecialSections(specialSections.filter((_, i) => i !== index));
    };


    // Standalone Fields
    const startCreatingField = () => {
        setIsCreatingField(true);
        setCurrentField({
            label: "",
            type: "text",
            options: [],
            isRequired: false
        });
    };
    
    const handleStandaloneFieldChange = (key, value) => {
        setCurrentField((prevField) => ({
            ...prevField,
            [key]: value
        }));
    };
    
    const handleStandaloneOptionsChange = (options) => {
        setCurrentField((prevField) => ({
            ...prevField,
            options: options
        }));
    };
    
    const finishField = () => {
        const normalizedFieldName = currentField.label.trim().toLowerCase();
        const existingFieldNames = standaloneFields.map(field => field.label.trim().toLowerCase());
    
        if (currentField.label.trim() === "") {
            alert("Field label cannot be empty.");
            return;
        }
        if (currentField.label.length > 50) {
            alert("Field label cannot exceed 50 characters.");
            return;
        }
        if (existingFieldNames.includes(normalizedFieldName)) {
            alert(`Field label must be unique. A field with the label "${currentField.label}" already exists.`);
            return;
        }
    
        setStandaloneFields([...standaloneFields, currentField]);
        setIsCreatingField(false);
    };
    
    
    const removeStandaloneField = (index) => {
        setStandaloneFields(standaloneFields.filter((_, i) => i !== index));
    };



    return (
        <>
        {!showCreateJob &&
        <div className="special-form">
            <button type="button" className="" onClick={() => setShowCreateJob(true)}>Back</button>
            <h1 style={{ textAlign: "center" }}>Create Form with Simulation</h1>
            <form>
            <div className="section-buttons-container">
                {!trackedSections.includes("Personal Information") && (
                    <button type="button" className="section-button" onClick={() => addSection("Personal Information")}>Add Personal Section</button>
                )}
                {!trackedSections.includes("Education") && (
                    <button type="button" className="section-button" onClick={() => addSection("Education")}>Add Education Section</button>
                )}
                {!trackedSections.includes("Experience") && (
                    <button type="button" className="section-button" onClick={() => addSection("Experience")}>Add Experience Section</button>
                )}
                {!trackedSections.includes("Skills") && (
                    <button type="button" className="section-button" onClick={() => addSection("Skills")}>Add Skills Section</button>
                )}
            </div>
            {trackedSections.includes("Personal Information") && (
                <div className="section-container">
                    <PersonalSection onChange={setPersonalData} />
                    <button type="button" className="remove-section-button" onClick={() => removeSectionByName("Personal Information")}>X</button>
                </div>
            )}
            {trackedSections.includes("Education") && (
                <div className="section-container">
                    <EducationSection onChange={setEducationData} />
                    <button type="button" className="remove-section-button" onClick={() => removeSectionByName("Education")}>X</button>
                </div>
            )}
            {trackedSections.includes("Experience") && (
                <div className="section-container">
                    <ExperienceSection onChange={setExperienceData} />
                    <button type="button" className="remove-section-button" onClick={() => removeSectionByName("Experience")}>X</button>
                </div>
            )}
            {trackedSections.includes("Skills") && (
                <div className="section-container">
                    <SkillSection onChange={setSkillData} />
                    <button type="button" className="remove-section-button" onClick={() => removeSectionByName("Skills")}>X</button>
                </div>
            )}
    
                {specialSections.map((section, index) => (
                    <div key={index} className="section-container">
                        <SpecialSection
                            sectionName={section.sectionName}
                            labels={section.labels}
                            fieldType={section.fieldType}
                            fieldOptions={section.fieldOptions}
                            Mandatory={section.Mandatory}
                            onChange={() => {}}
                        />
                        <button type="button" className="remove-section-button" onClick={() => removeSection(index)}>X</button>
                        {/* <button className="close-button-x" onClick={() => removeSection(index)}>
                            &times;
                        </button> */}
                    </div>
                ))}
    
                {isCreatingSection && (
                    <div className="special-section">
                        <input
                            type="text"
                            placeholder="Section Name"
                            value={currentSection.sectionName}
                            onChange={(e) => {
                                if (e.target.value.length <= 50) {
                                    setCurrentSection({ ...currentSection, sectionName: e.target.value });
                                } else {
                                    alert("Section Name cannot exceed 50 characters.");
                                }
                            }}
                        />
                        {currentSection.labels.map((label, fieldIndex) => (
                            <div key={fieldIndex} className="field-group">
                                <input
                                    type="text"
                                    placeholder="Field Label"
                                    value={label}
                                    onChange={(e) => {
                                        if (e.target.value.length <= 50) {
                                            handleFieldChange(fieldIndex, "labels", e.target.value);
                                        } else {
                                            alert("Field Label cannot exceed 50 characters.");
                                        }
                                    }}
                                />
                                <select
                                    value={currentSection.fieldType[fieldIndex]}
                                    onChange={(e) => handleFieldChange(fieldIndex, "fieldType", e.target.value)}
                                >
                                    <option value="text">Text</option>
                                    <option value="textarea">TextArea</option>
                                    <option value="date">Date</option>
                                    <option value="year">Year</option>
                                    <option value="email">Email</option>
                                    <option value="phone">Phone</option>
                                    <option value="url">URL</option>
                                    <option value="dropdown">Dropdown</option>
                                    <option value="radio">Radio</option>
                                    <option value="checkbox">Checkbox</option>
                                </select>
                                {["dropdown", "radio", "checkbox"].includes(currentSection.fieldType[fieldIndex]) && (
                                    <input
                                        type="text"
                                        placeholder="Options (comma separated)"
                                        value={currentSection.fieldOptions[fieldIndex].join(",")}
                                        onChange={(e) => handleOptionsChange(fieldIndex, e.target.value.split(","))}
                                    />
                                )}
                                <label>
                                    Mandatory
                                    <input
                                        type="checkbox"
                                        checked={currentSection.Mandatory[fieldIndex]}
                                        onChange={(e) => handleFieldChange(fieldIndex, "Mandatory", e.target.checked)}
                                    />
                                </label>
                                <br />
                                <button type="button" className="form-button" onClick={() => removeField(fieldIndex)}>Remove Field</button>
                                <button type="button" className="form-button" onClick={addField}>Insert Field</button>
                            </div>
                        ))}
                        
                        <button type="button" className="form-button" onClick={finishSection}>Add Section</button>
                    </div>
                )}
    
                {!isCreatingSection && (
                    <button type="button" className="form-button" onClick={startCreatingSection}>Create Custom Section</button>
                )}
    
                {standaloneFields.map((field, index) => (
                    <div key={index} className="section-container">
                        <SpecialField
                            label={field.label}
                            fieldType={field.type}
                            fieldOptions={field.options}
                            isMandatory={field.isRequired}
                            onChange={() => {}}
                        />
                        <button type="button" className="remove-field-button" onClick={() => removeStandaloneField(index)}>X</button>
                        {/* <button className="close-button-x" onClick={() => removeStandaloneField(index)}>
                            &times;
                        </button> */}
                    </div>
                ))}
    
                {isCreatingField && (
                    <div className="special-field">
                        <input
                            type="text"
                            placeholder="Field Label"
                            value={currentField.label}
                            onChange={(e) => {
                                if (e.target.value.length <= 50) {
                                    handleStandaloneFieldChange("label", e.target.value);
                                } else {
                                    alert("Field Label cannot exceed 50 characters.");
                                }
                            }}
                        />
                        <select
                            value={currentField.type}
                            onChange={(e) => handleStandaloneFieldChange("type", e.target.value)}
                        >
                            <option value="text">Text</option>
                            <option value="textarea">TextArea</option>
                            <option value="date">Date</option>
                            <option value="year">Year</option>
                            <option value="email">Email</option>
                            <option value="phone">Phone</option>
                            <option value="url">URL</option>
                            <option value="dropdown">Dropdown</option>
                            <option value="radio">Radio</option>
                            <option value="checkbox">Checkbox</option>
                        </select>
                        {["dropdown", "radio", "checkbox"].includes(currentField.type) && (
                            <input
                                type="text"
                                placeholder="Options (comma separated)"
                                value={currentField.options.join(",")}
                                onChange={(e) => handleStandaloneOptionsChange(e.target.value.split(","))}
                            />
                        )}
                        <label>
                            Mandatory
                            <input
                                type="checkbox"
                                checked={currentField.isRequired}
                                onChange={(e) => handleStandaloneFieldChange("isRequired", e.target.checked)}
                            />
                        </label>
                        <br />
                        <button type="button" className="form-button" onClick={finishField}>Add Field</button>
                    </div>
                )}
    
                {!isCreatingField && (
                    <button type="button" className="form-button" onClick={startCreatingField}>Create Custom Field</button>
                )}
    
                <button type="button" className="form-button" onClick={handleLogData}>Save</button>
    
            </form>
        </div>}
        {showCreateJob && <CreateJob detailsHistory={jobDetails} />}
        </>
    );
}