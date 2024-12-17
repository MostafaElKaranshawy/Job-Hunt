import React, { useState } from "react";
import SimpleText from "../InputTypes/SimpleText";
import TextArea from "../InputTypes/TextArea";
import DateInput from "../InputTypes/DateInput";
import DropDown from "../InputTypes/DropDown";
import { Slider, Typography } from "@mui/material";
import CreateForm from "../createForm/CreateForm.jsx";
import "../specialForm/specialForm.css";

export default function CreateJob() {
    const [jobTitle, setJobTitle] = useState("");
    const [jobDesc, setJobDesc] = useState("");
    const [jobCategory, setJobCategory] = useState("");
    const [jobLocation, setJobLocation] = useState("");
    const [jobLevel, setJobLevel] = useState("");
    const [jobType, setJobType] = useState("");
    const [jobDeadline, setJobDeadline] = useState("");
    const [salaryRange, setSalaryRange] = useState([0, 0]);
    const [showCreateForm, setShowCreateForm] = useState(false);

    const locations = ["Hybrid", "Onsite", "Remote"];
    const types = ["Full time", "Part time", "Internship", "Temporary"];
    const levels = ["Entry", "Junior", "Mid", "Senior", "Executive"];
    const [jobDetails, setJobDetails] = useState();

    const handleSalaryChange = (event, newValue) => {
        setSalaryRange(newValue);
    };

    const handleLogData = () => {
        const tempDetails = {
            title: jobTitle,
            description: jobDesc,
            category: jobCategory,
            workLocation: jobLocation,
            level: jobLevel,
            employmentType: jobType,
            deadline: jobDeadline,
            salaryRange: `${salaryRange[0]} - ${salaryRange[1]} USD per year`
           
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
                                <DropDown name="workLocation" options={locations} value={jobLocation} onChange={setJobLocation} />
                                <br />
                                <DropDown name="Level" options={levels} value={jobLevel} onChange={setJobLevel} />
                                <br />
                                <DropDown name="employmentType" options={types} value={jobType} onChange={setJobType} />
                                <br />
                                <DateInput name="Deadline" value={jobDeadline} onChange={setJobDeadline} />
                                <br />
                                {/* Salary Range Slider */}
                                <div className="salary-slider">
                                    <label>Salary Range</label>
                                    <Slider
                                        value={salaryRange}
                                        onChange={handleSalaryChange}
                                        valueLabelDisplay="auto"
                                        min={500}
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
                <CreateForm jobDetails={jobDetails} />
            )}
        </>
    );
}