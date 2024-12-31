import React from 'react';
import './styles/jobDetails.css';

const JobDetails = ({ job }) => {
  return (
    <div className="job-details">
      <h2>{job.jobTitle}</h2>
      <div className="job-info">
        <p><strong>Job ID:</strong> {job.jobId}</p>
        <p><strong>Company:</strong> {job.company}</p>
        <p><strong>Location:</strong> {job.location}</p>
        <p><strong>Salary:</strong> {job.salary}</p>
        <p><strong>Description:</strong></p>
        <p className="job-description">{job.description}</p>
      </div>
    </div>
  );
}

export default JobDetails;