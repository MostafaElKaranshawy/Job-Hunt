import React, { useState } from "react";
import "./createJob.css";
import SimpleText from "../InputTypes/SimpleText";
import TextArea from "../InputTypes/TextArea";
import DateInput from "../InputTypes/DateInput";
import DropDown from "../InputTypes/DropDown";
import { Slider, Typography } from "@mui/material";
import CreateForm from "../createForm/CreateForm.jsx";

export default function CreateJob() {
    const [jobTitle, setJobTitle] = useState("");
    const [jobDesc, setJobDesc] = useState("");
    const [jobCategory, setJobCategory] = useState("");
    const [jobLocation, setJobLocation] = useState("");
    const [jobLevel, setJobLevel] = useState("");
    const [jobDeadline, setJobDeadline] = useState("");
    const [salaryRange, setSalaryRange] = useState([0, 0]);
    const [showCreateForm, setShowCreateForm] = useState(false); // State to toggle the CreateForm component

    const locations = ["Hybrid", "On-location", "Online"];
    const levels = ["Entry", "Junior", "Mid", "Senior", "Executive"];

    const handleSalaryChange = (event, newValue) => {
        setSalaryRange(newValue);
    };

    const handleLogData = () => {
        const jobDetails = {
            title: jobTitle,
            description: jobDesc,
            category: jobCategory,
            location: jobLocation,
            level: jobLevel,
            deadline: jobDeadline,
            salaryRange: {
                min: salaryRange[0],
                max: salaryRange[1],
            },
        };

        // Validation
        if (!jobDetails.title.trim()) {
            alert("Title cannot be empty.");
            return;
        }
        if (!jobDetails.location.trim()) {
            alert("Location cannot be empty.");
            return;
        }
        if (!jobDetails.category.trim()) {
            alert("Category cannot be empty.");
            return;
        }
        if (!jobDetails.description.trim()) {
            alert("Description cannot be empty.");
            return;
        }
        if (!jobDetails.level.trim()) {
            alert("Level cannot be empty.");
            return;
        }
        if (!jobDetails.deadline.trim()) {
            alert("Deadline cannot be empty.");
            return;
        }

        console.log("Job Details:", jobDetails);

        // Show CreateForm component
        setShowCreateForm(true);
    };

    return (
        <div className="create-job">
            {!showCreateForm ? (
                <>
                    <h1 style={{ textAlign: "center" }}>Create Job</h1>
                    <div className="section-container">
                        <div className="section">
                            <SimpleText name="Title" value={jobTitle} onChange={setJobTitle} />
                            <br />
                            <TextArea name="Description" value={jobDesc} onChange={setJobDesc} />
                            <br />
                            <SimpleText name="Category" value={jobCategory} onChange={setJobCategory} />
                            <br />
                            <DropDown name="Location" options={locations} value={jobLocation} onChange={setJobLocation} />
                            <br />
                            <DropDown name="Level" options={levels} value={jobLevel} onChange={setJobLevel} />
                            <br />
                            <DateInput name="Deadline" value={jobDeadline} onChange={setJobDeadline} />
                            <br />
                            {/* Salary Range Slider */}
                            <div className="salary-slider">
                                <Typography variant="h6" gutterBottom>
                                    Salary Range
                                </Typography>
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
                        Create job form
                    </button>
                </>
            ) : (
                <CreateForm />
            )}
        </div>
    );
}
