import React, { useEffect, useState } from "react";
import SimpleText from "../InputTypes/SimpleText";
import TextArea from "../InputTypes/TextArea";
import DateInput from "../InputTypes/DateInput";
import DropDown from "../InputTypes/DropDown";
import CreateForm from "../createForm/CreateForm.jsx";
import "../specialForm/specialForm.css";

export default function CreateJob({detailsHistory, whenClose}) {
    const [jobTitle, setJobTitle] = useState(detailsHistory.title);
    const [jobDesc, setJobDesc] = useState(detailsHistory.description);
    const [jobCategory, setJobCategory] = useState(detailsHistory.category);
    const [countryLocation, setCountryLocation] = useState(detailsHistory.location);
    const [jobLocation, setJobLocation] = useState(detailsHistory.workLocation);
    const [jobLevel, setJobLevel] = useState(detailsHistory.level);
    const [jobType, setJobType] = useState(detailsHistory.employmentType);
    const [jobDeadline, setJobDeadline] = useState(detailsHistory.deadline);
    const [jobSalary, setJobSalary] = useState(detailsHistory.salary);
    const [showCreateForm, setShowCreateForm] = useState(false);

    const locations = ["ONSITE","REMOTE","HYBRID"];
    const types = ["FULL_TIME","PART_TIME","INTERNSHIP","TEMPORARY"];
    const levels = ["JUNIOR_LEVEL","MID_LEVEL","SENIOR_LEVEL","ENTRY_LEVEL","EXECUTIVE"];
    const [jobDetails, setJobDetails] = useState();



    const handleLogData = () => {

        const tempDetails = {
            title: jobTitle,
            description: jobDesc,
            category: jobCategory,
            location: countryLocation,
            workLocation: jobLocation,
            level: jobLevel,
            employmentType: jobType,
            deadline: `${jobDeadline}T00:00:00`,
            salary: jobSalary,
           
        };

        if (!tempDetails.title.trim()) {
            alert("Title cannot be empty.");
            return;
        }
        if (!tempDetails.description.trim()) {
            alert("Description cannot be empty.");
            return;
        }
        if (!tempDetails.category.trim()) {
            alert("Category cannot be empty.");
            return;
        }
        if (!tempDetails.location.trim()) {
            alert("Location cannot be empty.");
            return;
        }
        if (!tempDetails.workLocation.trim()) {
            alert("Work Location cannot be empty.");
            return;
        }
        
        if (!tempDetails.level.trim()) {
            alert("Level cannot be empty.");
            return;
        }
        if (!tempDetails.employmentType.trim()) {
            alert("Employment Type cannot be empty.");
            return;
        }
        if (!tempDetails.deadline.trim()) {
            alert("Deadline cannot be empty.");
            return;
        }
        if (!tempDetails.salary.trim()) {
            alert("Salary cannot be empty.");
            return;
        }

        console.log("Job Details:", tempDetails);
        setJobDetails(tempDetails);

        // Show CreateForm component
        setShowCreateForm(true);
    };

    const today = new Date().toISOString().split('T')[0];

    const handleDateChange = (date) => {
        if (date >= today) {
            // const date1 = date.target.value;
            // const dateTime = `${date1}T00:00:00`;
            setJobDeadline(date);
        } else {
            alert("Please select a date from today onwards.");
        }
    };


    return (
        <>
            {!showCreateForm ? (
                <div className="special-form">
                    <h1 style={{ textAlign: "center" }}>Create Job</h1>
                    <form>
                        <div className="section-container">
                            <div className="section">
                                <SimpleText name="Title" value={jobTitle} onChange={setJobTitle} />
                                <br />
                                <TextArea name="Description" value={jobDesc} onChange={setJobDesc} />
                                <br />
                                <SimpleText name="Category" value={jobCategory} onChange={setJobCategory} />
                                <br />
                                <SimpleText name="Country Location" value={countryLocation} onChange={setCountryLocation} />
                                <br />
                                <DropDown name="workLocation" options={locations} value={jobLocation} onChange={setJobLocation} />
                                <br />
                                <DropDown name="Level" options={levels} value={jobLevel} onChange={setJobLevel} />
                                <br />
                                <DropDown name="employmentType" options={types} value={jobType} onChange={setJobType} />
                                <br />
                                <DateInput name="Deadline" value={jobDeadline} onChange={handleDateChange} />
                                <br />
                                <label className="input-label">Yearly salary in $</label>
                                <br />
                                <input
                                type="number"
                                value={jobSalary}
                                onChange={(e) => setJobSalary(e.target.value)}
                                required
                                />
                                
                            </div>
                        </div>
                        <button type="button" className="form-button" onClick={handleLogData}>
                            Create Job Form
                        </button>
                    </form>
                </div>
            ) : (
                <CreateForm jobDetails={jobDetails} whenSave={whenClose} />
            )}
        </>
    );
}
CreateJob.defaultProps = {
    detailsHistory: {
        title: "",
        description: "",
        category: "",
        workLocation: "",
        level: "",
        employmentType: "",
        deadline: "",
        salaryRange: [0,0],
    }
}