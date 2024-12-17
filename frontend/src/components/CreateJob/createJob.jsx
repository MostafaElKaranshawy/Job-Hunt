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
    const [showCreateForm, setShowCreateForm] = useState(false); // State to toggle the CreateForm component

    const locations = ["Hybrid", "Onsite", "Remote"];
    const types = ["Full time", "Part time", "Internship", "Temporary"]
    const levels = ["Entry", "Junior", "Mid", "Senior", "Executive"];
    let jobDetails = {}

    const handleSalaryChange = (event, newValue) => {
        setSalaryRange(newValue);
    };

    const handleLogData = () => {
        jobDetails = {
            title: jobTitle,
            description: jobDesc,
            category: jobCategory,
            location: jobLocation,
            level: jobLevel,
            type: jobType,
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
                                <DropDown name="Location" options={locations} value={jobLocation} onChange={setJobLocation} />
                                <br />
                                <DropDown name="Level" options={levels} value={jobLevel} onChange={setJobLevel} />
                                <br />
                                <DropDown name="Type" options={types} value={jobType} onChange={setJobType} />
                                <br />
                                <DateInput name="Deadline" value={jobDeadline} onChange={setJobDeadline} />
                                <br />
                                {/* Salary Range Slider */}
                                <div className="salary-slider">
                                    {/* <Typography variant="h6" gutterBottom>
                                        Salary Range
                                    </Typography> */}
                                    <label>
                                    Salary Range
                                    </label>
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
                <CreateForm jobDetails={jobDetails}/>
            )}
        </>
    );
}
