import React, { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import './reportForm.css';

const ReportForm = () => {
    const [reason, setReason] = useState("");
    const [description, setDescription] = useState("");

    const { jobId } = useParams();
    const navigate = useNavigate();

    const handleReasonChange = (e) => {
        setReason(e.target.value);
    };

    const handleDescriptionChange = (e) => {
        setDescription(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const reportData = {
            reason,
            description,
        };
        const url = `http://localhost:8080/job/${jobId}/report`;
        try {
            const response = await fetch(url, {
                method: 'POST',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(reportData),
            });

            if (response.ok) {
                alert("Report submitted successfully!");
                navigate(-1); // Navigate back to the previous page
            } else {
                alert("Failed to submit report.");
            }
        } catch (error) {
            console.error("Error submitting report:", error);
            alert("An error occurred while submitting the report.");
        }
    };

    const handleClose = () => {
        navigate(-1); // Navigate back to the previous page
    };

    return (
        <div className="report-form-overlay">
            <div className="report-form-container">
                <button className="close-button" onClick={handleClose}>
                    &times;
                </button>
                <h2>Report Job</h2>
                <form onSubmit={handleSubmit}>
                    <label>
                        Reason:
                        <select value={reason} onChange={handleReasonChange} required>
                            <option value="">Select a reason</option>
                            <option value="INAPPROPRIATE_CONTENT">Inappropriate Content</option>
                            <option value="SCAM">Scam</option>
                            <option value="DISCRIMINATION">Discrimination</option>
                            <option value="ILLEGAL_ACTIVITY">Illegal Activity</option>
                            <option value="OTHER">Other</option>
                        </select>
                    </label>
                    <label>
                        Description:
                        <textarea
                            value={description}
                            onChange={handleDescriptionChange}
                            required
                        />
                    </label>
                    <button type="submit">Submit Report</button>
                </form>
            </div>
        </div>
    );
};

export default ReportForm;