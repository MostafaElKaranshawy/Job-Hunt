import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import "./jobsApplicationSection.css";

export default function JobsApplicationSection() {
    
    const { userName } = useParams();
    const [applications, setApplications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Pagination states
    const [currentApplicationIndex, setCurrentApplicationIndex] = useState(0);

    useEffect(() => {
        const fetchApplications = async () => {
            try {
                const response = await fetch(`http://localhost:8080/home/${userName}/applications`);
                console.log(response);
                if (!response.ok) {
                    throw new Error(`Error: ${response.statusText}`);
                }
                const data = await response.json();
                setApplications(data);
            } catch (err) {
                console.error(err); // Log for debugging
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchApplications();
    }, [userName]);

    // Handle next and previous navigation
    const goToNextApplication = () => {
        if (currentApplicationIndex < applications.length - 1) {
            setCurrentApplicationIndex(currentApplicationIndex + 1);
        }
    };

    const goToPreviousApplication = () => {
        if (currentApplicationIndex > 0) {
            setCurrentApplicationIndex(currentApplicationIndex - 1);
        }
    };

    if (loading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    if (applications.length === 0) {
        return <div>No job applications found.</div>;
    }

    const currentApplication = applications[currentApplicationIndex];

    return (
        <div className="job-applications-section">
            <h1>Job Application: {currentApplication.jobTitle}</h1>
            <p><strong>Status:</strong> {currentApplication.applicationStatus}</p>
            <p><strong>Date:</strong> {new Date(currentApplication.applicationDate).toLocaleString()}</p>
            
            {/* Display company info */}
            <p><strong>Company Name:</strong> {currentApplication.companyName}</p>
            <p><strong>Company Location:</strong> {currentApplication.companyAddress}</p>
            <p><strong>Company Website:</strong> {currentApplication.companyWebsite}</p>

            <table className="responses-table">
                <thead>
                    <tr>
                        <th>Question</th>
                        <th>Response</th>
                    </tr>
                </thead>
                <tbody>
                    {currentApplication.responses.map((response, index) => (
                        <tr key={index}>
                            <td>{response.fieldName}</td>
                            <td>{response.responseData}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* Pagination controls for applications */}
            <div className="pagination">
                <button
                    onClick={goToPreviousApplication}
                    disabled={currentApplicationIndex === 0}
                >
                    Previous
                </button>
                <span>
                    Application {currentApplicationIndex + 1} of {applications.length}
                </span>
                <button
                    onClick={goToNextApplication}
                    disabled={currentApplicationIndex === applications.length - 1}
                >
                    Next
                </button>
            </div>
        </div>
    );
}
