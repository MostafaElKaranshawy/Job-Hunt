import React from 'react';
import './JobList.css';

function JobList({ jobs, handleExpandJob }) {

    const truncateDescription = (description, limit = 100) => {
        return description.length > limit
            ? description.substring(0, limit) + '...'
            : description;
    };


    return (
        <div className="job-list">
            {jobs.map(job => (
                <div key={job.id} className="job-card">
                    <div className="job-card-header">
                        <div className="company-logo"></div>
                        <div className="job-info">
                            <h3>{job.company.name}</h3>
                            <div className="job-title">
                                {job.title}
                                {job.isNew && <span className="new-badge">New post</span>}
                            </div>
                            <div className="job-details">
                                <span>{job.location}</span>
                                <span>{job.type}</span>
                                <span>{job.level}</span>
                                <span>${job.salary}</span>
                                <span>{job.posted}</span>
                            </div>
                        </div>
                    </div>
                    <p className="job-description">{truncateDescription(job.description)}</p>

                    <button 
                        onClick={() => handleExpandJob(job)}
                        className="learn-more-button"
                    >Learn More</button>
                </div>
            ))}
        </div>
    );
}

export default JobList;