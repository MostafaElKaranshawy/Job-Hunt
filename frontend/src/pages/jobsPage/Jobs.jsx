import { Link } from 'react-router-dom';
import { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './Jobs.css';
import Sidebar from '../../components/sideBar/Sidebar';
import CreateJob from '../../components/CreateJob/createJob';
const backendURL = import.meta.env.VITE_BACKEND_URL;

function Jobs() {
  const { companyUsername } = useParams();
  const [activeJobs, setActiveJobs] = useState([]);
  const [expiredJobs, setExpiredJobs] = useState([]);
  const [menuVisible, setMenuVisible] = useState(null);
  const [showCreateJob, setShowCreateJob] = useState(false);
  const [currentPageActive, setCurrentPageActive] = useState(1);
  const [currentPageExpired, setCurrentPageExpired] = useState(1);
  const jobsPerPage = 5; // Number of jobs to display per page

  const fetchJobs = useCallback(async () => {
    try {
      const response = await fetch(`${backendURL}/company/${companyUsername}/jobs`, {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        const data = await response.json();
        console.log(data);
        setActiveJobs(data.active || []);
        setExpiredJobs(data.expired || []);
      } else {
        console.log('Failed fetching active jobs', response.statusText);
      }
    } catch (error) {
      console.log('Failed to fetch active jobs', error);
    }
  }, [companyUsername]);

  useEffect(() => {
    fetchJobs();
  }, [fetchJobs, companyUsername]);

  const deleteJob = async (jobID) => {
    try {
      const response = await fetch(`${backendURL}/company/${companyUsername}/jobs/${jobID}`, {
        method: 'DELETE',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (response.ok) {
        console.log(`Job ${jobID} deleted successfully`);
        fetchJobs();
      } else {
        console.log('Failed deleting job', response.statusText);
      }
    } catch (error) {
      console.log('Failed to delete job', error);
    }
  };

  const toggleMenu = (jobID) => {
    menuVisible === jobID ? setMenuVisible(null) : setMenuVisible(jobID);
  };

  const formatter = new Intl.DateTimeFormat('en-EG', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false,
  });

  // Get jobs for the current page
  const indexOfLastJobActive = currentPageActive * jobsPerPage;
  const indexOfFirstJobActive = indexOfLastJobActive - jobsPerPage;
  const currentActiveJobs = activeJobs.slice(indexOfFirstJobActive, indexOfLastJobActive);

  const indexOfLastJobExpired = currentPageExpired * jobsPerPage;
  const indexOfFirstJobExpired = indexOfLastJobExpired - jobsPerPage;
  const currentExpiredJobs = expiredJobs.slice(indexOfFirstJobExpired, indexOfLastJobExpired);

  // Change active page
  const paginateActive = (pageNumber) => setCurrentPageActive(pageNumber);

  // Change expired page
  const paginateExpired = (pageNumber) => setCurrentPageExpired(pageNumber);

  // Calculate total pages
  const totalActivePages = Math.ceil(activeJobs.length / jobsPerPage);
  const totalExpiredPages = Math.ceil(expiredJobs.length / jobsPerPage);

  const handleCreateJob = () => {
    setShowCreateJob(true);
}

const handleCloseModal = () => {
    setShowCreateJob(false);
}

  return (
    <div className='jobs'>
      <Sidebar />
      <div className="jobs-container">
        {/* Active Job Offers */}
        <div className="job-section">
          <h3>Active Job Offers</h3>
          <div className="card-container">
            {currentActiveJobs.map((job) => (
              <div className="card" key={job.id}>
                <div className="card-body">
                  <h5 className="card-title">
                    <b>{job.title}</b>
                  </h5>
                  <p className="card-text">{job.description}</p>
                  <p className="card-text"><b>Category:</b> {job.category}</p>
                  <p className="card-text"><b>Location:</b> {job.location}</p>
                  <p className="card-text"><b>posted at:</b> {formatter.format(new Date(job.postedAt))}</p>
                  <p className="card-text"><b>expires at:</b> {formatter.format(new Date(job.applicationDeadline))}</p>
                  <Link to={`/jobs/${job.id}`} className="btn btn-primary card-body-btn">View Applications</Link>
                  <i 
                    className="bi bi-three-dots-vertical card-body-icon"
                    onClick={() => toggleMenu(job.id)}
                  ></i>
                  <div className={`dropdown-menu small ${menuVisible === job.id ? 'show' : ''}`}>
                    <button
                      className="dropdown-item red"
                      onClick={() => {
                        console.log(`Delete job ${job.id}`);
                        deleteJob(job.id);
                        setMenuVisible(null);
                      }}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
          {/* Active Pagination Controls */}
          <div className="pagination">
            <button 
              className="page-button" 
              onClick={() => paginateActive(currentPageActive > 1 ? currentPageActive - 1 : 1)}>
              Previous
            </button>
            {[...Array(totalActivePages).keys()].map((pageNumber) => (
              <button 
                key={pageNumber} 
                className={`page-button ${pageNumber + 1 === currentPageActive ? 'active' : ''}`} 
                onClick={() => paginateActive(pageNumber + 1)}>
                {pageNumber + 1}
              </button>
            ))}
            <button 
              className="page-button" 
              onClick={() => paginateActive(currentPageActive < totalActivePages ? currentPageActive + 1 : totalActivePages)}>
              Next
            </button>
          </div>
        </div>

        {/* Expired Job Offers */}
        <div className="job-section">
          <h3>Expired Job Offers</h3>
          <div className="card-container">
            {currentExpiredJobs.map((job) => (
              <div className="card" key={job.id}>
                <div className="card-body">
                  <h5 className="card-title"><b>{job.title}</b></h5>
                  <p className="card-text">{job.description}</p>
                  <p className="card-text"><b>Category:</b> {job.category}</p>
                  <p className="card-text"><b>Location:</b> {job.location}</p>
                  <p className="card-text"><b>posted at:</b> {formatter.format(new Date(job.postedAt))}</p>
                  <p className="card-text"><b>expired at:</b> {formatter.format(new Date(job.applicationDeadline))}</p>
                  <Link to={`/jobs/${job.id}`} className="btn btn-primary card-body-btn">View Applications</Link>
                  <i 
                    className="bi bi-three-dots-vertical card-body-icon"
                    onClick={() => toggleMenu(job.id)}
                  ></i>
                  <div className={`dropdown-menu small ${menuVisible === job.id ? 'show' : ''}`}>
                    <button
                      className="dropdown-item red"
                      onClick={() => {
                        console.log(`Delete job ${job.id}`);
                        deleteJob(job.id);
                        setMenuVisible(null);
                      }}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
          {/* Expired Pagination Controls */}
          <div className="pagination">
            <button 
              className="page-button" 
              onClick={() => paginateExpired(currentPageExpired > 1 ? currentPageExpired - 1 : 1)}>
              Previous
            </button>
            {[...Array(totalExpiredPages).keys()].map((pageNumber) => (
              <button 
                key={pageNumber} 
                className={`page-button ${pageNumber + 1 === currentPageExpired ? 'active' : ''}`} 
                onClick={() => paginateExpired(pageNumber + 1)}>
                {pageNumber + 1}
              </button>
            ))}
            <button 
              className="page-button" 
              onClick={() => paginateExpired(currentPageExpired < totalExpiredPages ? currentPageExpired + 1 : totalExpiredPages)}>
              Next
            </button>
          </div>
        </div>
      </div>

      <button className="btn btn-primary custom-btn" onClick={handleCreateJob}>+ Create</button>

      {/* Modal */}
      {showCreateJob && (
        <div className="modal show d-block" tabIndex="-1">
            <div className="modal-dialog custom-modal-size"> {/* Custom class */}
                <div className="modal-content">
                    <div className="modal-header">
                        <h5 className="modal-title">Create Job</h5>
                    </div>
                    <div className="modal-body">
                        <CreateJob whenClose={handleCloseModal}/>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" onClick={handleCloseModal}>Close</button>
                    </div>
                </div>
            </div>
        </div>
        )}
    </div>
  );
}

export default Jobs;

