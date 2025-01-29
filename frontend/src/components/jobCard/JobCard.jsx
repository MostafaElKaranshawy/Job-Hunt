import React, {useState} from 'react';
import './JobCard.css';
import { truncateDescription, calculateRelativeTime } from '../../utils/userHomeUtils';
import { locationOptions, employmentTypes, jobLevels } from '../../constants/filterOptions';
import profileHolder from "../../assets/profile.png";
function JobCard({ job, handleExpandJob, handleToggleSave }) {

    const [saved, setSaved] = useState(job.saved);
    const [saveLoading, setSaveLoading] = useState(false);



    const handleSaveClick = async (e) => {
        if (saveLoading) return;

        e.preventDefault();
        e.stopPropagation();

        setSaveLoading(true);
        await handleToggleSave(job, saved, setSaved);
        console.log(job)

        setSaveLoading(false);
    };

    return (
        <div className="job-card">
            <div className="job-card-header">
                <div className="company-logo">
                    <img src={profileHolder} />
                </div>
                <div className="job-info">
                    <div className="name-and-save">
                        <h3>{job.company.name}</h3>
                        <i
                            className={`fa-bookmark save-icon
                                        ${saved ? 'saved fa-solid' : 'fa-regular'}`}
                            onClick={handleSaveClick}
                        ></i>
                    </div>
                    <div className="job-title">
                        {job.title}
                    </div>
                    <div className="job-details">
                        <span>{locationOptions.find((option) => option.id === job.workLocation)?.label}</span>
                        <span>{employmentTypes.find((option) => option.id === job.employmentType)?.label}</span>
                        <span>{jobLevels.find((option) => option.id === job.level)?.label}</span>
                        <span>${job.salary}</span>
                        <span>{job.posted}</span>
                        <span>{calculateRelativeTime(job.postedAt)}</span>
                    </div>
                </div>
            </div>
            <p className="job-description">{truncateDescription(job.description)}</p>

            {/* add the apply and don't apply when already applied */}
            <button
                onClick={() => handleExpandJob(job, saved, setSaved)}
                className="learn-more-button"
            >
                Learn More
            </button>
        </div>
    );
}

export default JobCard;
