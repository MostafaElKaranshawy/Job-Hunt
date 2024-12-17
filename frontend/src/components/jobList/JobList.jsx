import React from 'react';
import './JobList.css';
import { truncateDescription ,calculateRelativeTime } from '../../utils/userHomeUtils';
import { locationOptions, employmentTypes, jobLevels, minimumSalary } from '../../constants/filterOptions';

function JobList({ jobs, handleExpandJob }) {

        

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
                            </div>
                            <div className="job-details">
                                <span>{locationOptions.find((option) => option.id === job.workLocation).label}</span>
                                <span>{employmentTypes.find((option) => option.id === job.employmentType).label}</span>
                                <span>{jobLevels.find((option) => option.id === job.level).label}</span>
                                <span>${job.salary}</span>
                                <span>{job.posted}</span>
                                <span>{calculateRelativeTime(job.postedAt)}</span>
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