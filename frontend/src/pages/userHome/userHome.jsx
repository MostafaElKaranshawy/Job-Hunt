import React, { useState, useEffect } from "react"
import Header from "../../components/header/header";
import SearchBar from "../../components/homeSearch/SearchBar";
import Filters from "../../components/filters/Filters";
import JobList from "../../components/jobList/JobList";
import './userHome.css'

import { fetchJobs } from "../../services/homeService";
import Sorting from "../../components/sorting/Sorting.jsx";

function UserHome() {

    const [jobs, setJobs] = useState([]);
    const [expandedJob, setExpandedJob] = useState(null);

    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const offset = 10;

    const [filters, setFilters] = useState({
        location: "",
        employmentType: "",
        jobLevel: "",
        salary: 'any',
        searchQuery: "",
        sortBy: "DateDesc",
    });


    useEffect(() => {
        const loadJobs = async () => {
            setLoading(true);
            try {
                const jobs = await fetchJobs(filters, page, offset);
                setJobs(jobs); // Adjust based on your backend's response structure
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        loadJobs();
    }, [filters, page]);


    const handleSortChange = (value) => {
        setFilters((prevFilters) => ({
            ...prevFilters,
            sortBy: value,
        }));
    };



    const handleExpandJob = (job) => {
        setExpandedJob(job);
    };

    const handleCloseExpandedJob = () => {
        setExpandedJob(null);
    };


    const handleFilterChange = (filterName, value) => {
        setFilters((prevFilters) => ({
            ...prevFilters,
            [filterName]: value,
        }));
    };


    const handleSearch = (searchQuery) => {
        setFilters((prevFilters) => ({
            ...prevFilters,
            searchQuery,
        }));
    };



    return (
        <div className="home">
            <Header />
            <main className="main-content">
                <div className="hero-section">
                    <h1>Find your <span className="highlight">right jobs offers</span> today</h1>
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
                        <Sorting
                            sortBy={filters.sortBy}
                            onSortChange={handleSortChange}
                        />
                        {loading ?
                            <p>Loading jobs...</p> :
                            jobs.length === 0 ? 
                            <p className="no-jobs-message">No matching jobs found</p> :
                            <JobList jobs={jobs} handleExpandJob={handleExpandJob} />
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
                                <h3>{expandedJob.company.name}</h3>
                                <div className="job-title">
                                    {expandedJob.title}
                                    {expandedJob.isNew && <span className="new-badge">New post</span>}
                                </div>
                                <div className="job-details">
                                    <span>{expandedJob.location}</span>
                                    <span>{expandedJob.type}</span>
                                    <span>{expandedJob.level}</span>
                                    <span>${expandedJob.salary}</span>
                                    <span>{expandedJob.posted}</span>
                                </div>
                            </div>

                        </div>

                        {/* Apply button */}
                        <button className="apply-button">Apply Now</button>

                        <p className="job-description">{expandedJob.description}</p>

                    </div>
                </div>
            )}
        </div>
    )
}

export default UserHome;