import React, { useEffect, useState } from "react";
import SimpleText from "../InputTypes/SimpleText";
import TextArea from "../InputTypes/TextArea";
import DateInput from "../InputTypes/DateInput";
import DropDown from "../InputTypes/DropDown";
import { Slider, Typography } from "@mui/material";
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
    const [salaryRange, setSalaryRange] = useState([detailsHistory.salaryRange[0], detailsHistory.salaryRange[1]]);
    const [showCreateForm, setShowCreateForm] = useState(false);

    const locations = ["Hybrid", "Onsite", "Remote"];
    const types = ["Full time", "Part time", "Internship", "Temporary"];
    const levels = ["Entry", "Junior", "Mid", "Senior", "Executive"];
    const [jobDetails, setJobDetails] = useState();

    // useEffect(() => {
    //     console.log("HI HI Captain");
    //     console.log(detailsHistory);
    // },[])

    const handleSalaryChange = (event, newValue) => {
        setSalaryRange(newValue);
    };

    const handleLogData = () => {
        const tempDetails = {
            title: jobTitle,
            description: jobDesc,
            category: jobCategory,
            location: countryLocation,
            workLocation: jobLocation,
            level: jobLevel,
            employmentType: jobType,
            deadline: jobDeadline,
            salaryRange: [salaryRange[0],salaryRange[1]],
           
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

        console.log("Job Details:", tempDetails);
        setJobDetails(tempDetails);

        // Show CreateForm component
        setShowCreateForm(true);
    };

    const today = new Date().toISOString().split('T')[0];

    const handleDateChange = (date) => {
        if (date >= today) {
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
                                {/* Salary Range Slider */}
                                <div className="salary-slider">
                                    <label className="input-label">Salary Range</label>
                                    <Slider
                                        value={salaryRange}
                                        onChange={handleSalaryChange}
                                        valueLabelDisplay="auto"
                                        min={0}
                                        max={200000}
                                        step={500}
                                    />
                                    <div style={{ display: "flex", justifyContent: "space-between" }}>
                                        <Typography variant="body1">Min: ${salaryRange[0]}</Typography>
                                        <Typography variant="body1">Max: ${salaryRange[1]}</Typography>
                                    </div>
                                </div>
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