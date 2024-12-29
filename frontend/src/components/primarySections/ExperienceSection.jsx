import React, { useState, useEffect } from "react";
import SimpleText from "../InputTypes/SimpleText";
import TextArea from "../InputTypes/TextArea";
import DateInput from "../InputTypes/DateInput";
import DropDown from "../InputTypes/DropDown";
import EndDate from "../InputTypes/EndDate";

export default function ExperienceSection({ onChange }) {
    const [jobTitle, setJobTitle] = useState("");
    const [companyName, setCompanyName] = useState("");
    const [jobLocation, setJobLocation] = useState("");
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [jobDescription, setJobDescription] = useState("");
    const [currentRule, setCurrentRule] = useState(false);

    const locationTypes = ["onsite", "hybrid", "remote"];

    useEffect(() => {
        onChange({
            jobTitle,
            companyName,
            jobLocation,
            startDate,
            endDate,
            jobDescription,
            currentRule,
        });
    }, [jobTitle, companyName, jobLocation, startDate, endDate, jobDescription, currentRule]);

    return (
        <div className="section-container">
            <h2 className="section-header">Experience</h2>
            <div className="section">
                <SimpleText name="Job Title" value={jobTitle} onChange={setJobTitle} />
                <br />

                <SimpleText name="Company Name" value={companyName} onChange={setCompanyName} />
                <br />

                <DropDown name="Location Type" options={locationTypes} value={jobLocation} onChange={setJobLocation} isMust={false} />
                <br />

                <DateInput name="Start Date" value={startDate} onChange={setStartDate} />
                <br />

                <EndDate name="End Date" value={endDate} onChange={setEndDate} currentRule={currentRule} onCurrentRuleChange={setCurrentRule} />
                <br />

                <TextArea name="Job Description" value={jobDescription} onChange={setJobDescription} isMust={false} />
            </div>
        </div>
    );
}