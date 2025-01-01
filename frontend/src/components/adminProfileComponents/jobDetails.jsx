import React from 'react';
import './styles/jobDetails.css';

const JobDetails = ({ job }) => {
    const formattedDate = (date)=> { 
        return new Date(date).toLocaleString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: 'numeric',
            minute: 'numeric',
            hour12: true
        });
    }
    return (
        <div className="job-details">
            <h2>{job.jobTitle}</h2>
            <div className="job-info">
                <p><strong>Job ID:</strong> Job-{job.id}</p>
                <p><strong>Company:</strong> {job.company.name}</p>
                <p><strong>Job Title:</strong> {job.title}</p>
                <p><strong>Posted on:</strong> {formattedDate(job.postedAt)}</p>
                <p><strong>Employment Type:</strong> {job.employmentType}</p>
                <p><strong>Level:</strong> {job.level}</p>
                <p><strong>Category:</strong> {job.category}</p>
                <p><strong>Location:</strong> {job.location}</p>
                <p><strong>Salary:</strong> {job.salary}</p>
                <div className="job-description">
                    <p><strong>Description:</strong></p>
                    {job.description}
                </div>
            </div>
        </div>
    );
}

export default JobDetails;