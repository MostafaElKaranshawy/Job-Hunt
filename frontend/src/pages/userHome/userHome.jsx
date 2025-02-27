import React, { useState, useEffect } from "react"
import Header from "../../components/header/header";
import SearchBar from "../../components/searchBar/SearchBar";
import Filters from "../../components/filters/Filters";
// import JobList from "../../components/jobList/JobList";
import JobCard from "../../components/jobCard/JobCard";
import './userHome.css'

import { fetchJobs, toggleSaveJob } from "../../services/homeService";
import Sorting from "../../components/sorting/Sorting.jsx";
import { calculateRelativeTime } from "../../utils/userHomeUtils";
import { locationOptions, employmentTypes, jobLevels, minimumSalary } from '../../constants/filterOptions';
import { useNavigate } from "react-router-dom";


function UserHome() {

    const navigate = useNavigate();

    const [jobs, setJobs] = useState([]);
    const [expandedJob, setExpandedJob] = useState(null);
    const [currentJob, setCurrentJob] = useState({});
    const [expandedJobState, setExpandedJobState] = useState(null); // Store both saved state and setSaved function

    const [page, setPage] = useState(0);
    const [totalJobsCount, setTotalJobsCount] = useState(0); // New state for total jobs count

    const [loading, setLoading] = useState(false);
    const offset = 5;

    const [filters, setFilters] = useState({
        workLocation: "",
        // location: "",
        employmentType: "",
        jobLevel: "",
        salary: '0',
        searchQuery: "",
        sortBy: "DateDesc",
    });




    useEffect(() => {

        loadJobs();
    }, [filters, page]);

    const loadJobs = async () => {
        setLoading(true);
        try {
            const { jobs, totalJobs } = await fetchJobs(filters, page, offset);
            setJobs(jobs);
            setTotalJobsCount(totalJobs);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleSortChange = (value) => {
        setFilters((prevFilters) => ({
            ...prevFilters,
            sortBy: value,
        }));

        setPage(0);
    };

    const handleExpandJob = (job, saved, setSaved) => {
        setExpandedJob(job);
        setExpandedJobState({ saved: saved, setSaved }); // Store saved state and setSaved function
    };


    const handleCloseExpandedJob = () => {
        setExpandedJob(null);
    };


    const handleFilterChange = (filterName, value) => {
        setFilters((prevFilters) => ({
            ...prevFilters,
            [filterName]: value,
        }));

        setPage(0);
    };


    const handleSearch = (searchQuery) => {
        setFilters((prevFilters) => ({
            ...prevFilters,
            searchQuery,
        }));

        setPage(0);
    };


    const handleToggleSave = async (job, saved, setSaved) => {
        if (loading)
            return;

        try {
            await toggleSaveJob(job, saved);

            if (expandedJob){
                // console.log("expanded job")
                expandedJobState.setSaved((prevSaved) => !prevSaved);
                setExpandedJobState((prevExpandedJobState) => ({
                    ...prevExpandedJobState,
                    saved: !prevExpandedJobState.saved,
                }));
            }
            else
                setSaved((prevSaved) => !prevSaved);        


        }
        catch (err) {
            console.error(err);

        }


    }

    const startIndex = page * offset + 1;
    const endIndex = Math.min((page + 1) * offset, totalJobsCount);

    async function handleApplyClick (){
        navigate(`/user/apply/job/${expandedJob.id}/form`);
        setExpandedJob(null);
    };

    function handleReportClick(){
        navigate(`/user/job/${expandedJob.id}/report`);
    }

      
    return (
        <div className="home">
            <Header />
            <main className="main-content">
                <div className="hero-section">
                    <h1>Find your right jobs offers today</h1>
                    <p>Thousands of companies in the computer, engineering and technology sectors are waiting for you.</p>
                    <SearchBar
                        onSearch={handleSearch}
                    />
                </div>
                <div className="content-layout">
                    <aside className="filters-sidebar">
                        <Filters
                            filters={filters}
                            onFilterChange={handleFilterChange}
                        />
                    </aside>
                    <div className="job-content">

                        <div className="jobs-header">
                            <div className="job-range">
                                {totalJobsCount > 0 && !loading && (
                                    <span>{startIndex}-{endIndex} of {totalJobsCount}</span>
                                )}
                            </div>

                            <Sorting
                                sortBy={filters.sortBy}
                                onSortChange={handleSortChange}
                            />
                        </div>

                        {loading ?
                            <p className="loading-text">Loading jobs...</p> :
                            jobs.length === 0 ?
                                <p className="no-jobs-message">No matching jobs found</p> :

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

                    </div>
                </div>
            </main>

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
                        <div className="buttons-container">
                            <button
                                className={`apply-button  ${expandedJob.applied ? 'applied' : ''}`}
                                onClick={handleApplyClick}
                                disabled={expandedJob.applied}
                            >
                                {expandedJob.applied ?
                                    "Already Applied" : "Apply Now"
                                }

                            </button>
                            <br/>
                            <button className="apply-button" onClick={handleReportClick}>Report</button>
                        </div>

                        <p className="job-description">{expandedJob.description}</p>

                    </div>
                </div>
            )}
       
        </div>
    )
}

export default UserHome;