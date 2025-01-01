import React, { useEffect, useState } from "react";
import { toggleSaveJob } from "../../../services/homeService";
import { getSavedJobs } from "../../../services/userProfileService";
import JobCard from "../../jobCard/JobCard";
import { calculateRelativeTime } from "../../../utils/userHomeUtils";
import { locationOptions, employmentTypes, jobLevels } from '../../../constants/filterOptions';
import './savedJobsSection.css';

export default function SavedJobsSection() {
    const [jobs, setJobs] = useState([]);
    const [expandedJob, setExpandedJob] = useState(null);
    const [expandedJobState, setExpandedJobState] = useState(null); // Store both saved state and setSaved function


    const [page, setPage] = useState(0);
    const [totalJobsCount, setTotalJobsCount] = useState(0); // New state for total jobs count

    const [loading, setLoading] = useState(false);
    const offset = 5;
    const username = document.cookie.split("username=")[1];
    

    useEffect(() => {
        if (!username) return;
        loadJobs();
        
    },[username, page]);

    const loadJobs = async () => {
        setLoading(true);
        try {
            const {jobs, totalJobs} = await getSavedJobs(username, page, offset);
            setJobs(jobs);
            setTotalJobsCount(totalJobs);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };
    const handleExpandJob = (job, saved, setSaved) => {
        setExpandedJob(job);
        setExpandedJobState({ saved: saved, setSaved }); // Store saved state and setSaved function
    };
    const handleCloseExpandedJob = () => {
        setExpandedJob(null);
    };
    const handleToggleSave = async (job, saved, setSaved) => {
        if (loading)
            return;

        try {
            await toggleSaveJob(job, saved);

            await loadJobs();   

            setExpandedJob(null)
        }
        catch (err) {
            console.error(err);

        }


    }

    const startIndex = page * offset + 1;
    const endIndex = Math.min((page + 1) * offset, totalJobsCount);

    return (
        <div className="saved-jobs-section home">
            
            <div className="job-content">
                <div className="jobs-header">
                    <div className="job-range">
                        {totalJobsCount > 0 && !loading && (
                            <span>{startIndex}-{endIndex} of {totalJobsCount}</span>
                        )}
                    </div>
                </div>
                {loading ?
                    <p className="loading-text">Loading jobs...</p> :
                    jobs.length === 0 ?
                        <p className="no-jobs-message">No Saved jobs found</p> :
                        (
                            <div className="job-list">
                                {jobs.map((job) => (
                                    <JobCard
                                        key={job.id}
                                        job={job}
                                        handleExpandJob={handleExpandJob}
                                        handleToggleSave={handleToggleSave}
                                    />
                                ))}
                            </div>
                        )
                }
            </div>
            {
                totalJobsCount > 0 &&
                <div className="pagination">
                    <button
                        className="custom-pagination-button"
                        onClick={() => setPage(page - 1)}
                        disabled={page === 0 || loading}
                    >
                        &lt; Previous
                    </button>

                    <button
                        className="custom-pagination-button"
                        onClick={() => setPage(page + 1)}
                        disabled={endIndex >= totalJobsCount || loading}
                        data-testid="next-button"
                    >Next &gt;</button>
                </div>
            }
            {expandedJob && (
                <div className="expanded-job-overlay">
                    <div className="expanded-job-container">
                        <button className="close-button" onClick={handleCloseExpandedJob}>
                            &times;
                        </button>
                        <div className="job-card-header">
                            <div className="company-logo"></div>
                            <div className="job-info">
                                <div className="name-and-save">
                                    <h3>{expandedJob.company.name}</h3>

                                    <i
                                        className={`fa-bookmark save-icon expanded-save-icon
                                                    ${expandedJobState.saved ? 'saved fa-solid' : 'fa-regular'}`
                                        }
                                        onClick={() => handleToggleSave(expandedJob, expandedJobState.saved, expandedJobState.setSaved)}

                                    ></i>
                                </div>
                                <div className="job-title">
                                    {expandedJob.title}
                                    {expandedJob.isNew && <span className="new-badge">New post</span>}
                                </div>
                                <div className="job-details">
                                    <span>{locationOptions.find((option) => option.id === expandedJob.workLocation).label}</span>
                                    <span>{employmentTypes.find((option) => option.id === expandedJob.employmentType).label}</span>
                                    <span>{jobLevels.find((option) => option.id === expandedJob.level).label}</span>
                                    <span>${expandedJob.salary}</span>
                                    <span>{expandedJob.posted}</span>
                                    <span>{calculateRelativeTime(expandedJob.postedAt)}</span>
                                </div>
                            </div>

                        </div>
                        <button
                            className={`apply-button  ${expandedJob.applied ? 'applied' : ''}`}
                        >
                            {expandedJob.applied ?
                                "Already Applied" : "Apply Now"
                            }

                        </button>

                        <p className="job-description">{expandedJob.description}</p>

                    </div>
                </div>
            )}
        </div>
    )
}