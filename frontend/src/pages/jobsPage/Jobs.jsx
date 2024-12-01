import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './Jobs.css';
import Sidebar from '../../components/sideBar/Sidebar';

function Jobs() {
  const [activeJobs, setActiveJobs] = useState([]);
  const [expiredJobs, setExpiredJobs] = useState([]);
  const [menuVisible, setMenuVisible] = useState(null);

  const fetchActiveJobs = async () => {
    try{
        const response = await fetch('https://localhost:3000/company/jobs/active', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });
        if (response.ok) {
            const data = await response.json();
            setActiveJobs(data);
        }else{
            console.log('Failed to fetch active jobs', response.statusText);
        }
    }catch(error){
        console.log('Failed to fetch active jobs', error);
    }
  }

    useEffect(() => {
        fetchActiveJobs();
    }, [activeJobs]);

    const fetchExpiredJobs = async () => {
        try{
            const response = await fetch('https://localhost:3000/company/jobs/expired', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            });
            if (response.ok) {
                const data = await response.json();
                setExpiredJobs(data);
            }else{
                console.log('Failed to fetch expired jobs', response.statusText);
            }
        }catch(error){
            console.log('Failed to fetch expired jobs', error);
        }
    }

    useEffect(() => {
        fetchExpiredJobs();
    }, [expiredJobs]);

    const deleteJob = async (id) => {
        try{
            const response = await fetch(`https://localhost:3000/company/jobs/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                }
            });
            if (response.ok) {
                fetchActiveJobs();
                fetchExpiredJobs();
            }else{
                console.log('Failed to delete job', response.statusText);
            }
        }catch(error){
            console.log('Failed to delete job', error);
        }
    }

    // const editActiveJob = async (id, job) => { //////////////// ///////////////////
    //     try{
    //         const response = await fetch(`https://localhost:3000/company/jobs/${id}`, {
    //             method: 'PATCH',
    //             headers: {
    //                 'Content-Type': 'application/json',
    //             },
    //             body: JSON.stringify(job)
    //         });
    //         if (response.ok) {
    //             fetchActiveJobs();
    //         }else{
    //             console.log('Failed to edit job', response.statusText);
    //         }
    //     }catch(error){
    //         console.log('Failed to edit job', error);
    //     }
    // }

    const toggleMenu = (jobID) => {
        menuVisible === jobID ? setMenuVisible(null) : setMenuVisible(jobID);
    }



  useEffect(() => {
    setActiveJobs([
      { id: 1, title: 'Software Engineer', description: 'We are looking for a software engineer to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
      { id: 2, title: 'Product Manager', description: 'We are looking for a product manager to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
      { id: 3, title: 'Data Analyst', description: 'We are looking for a data analyst to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
      { id: 4, title: 'Graphic Designer', description: 'We are looking for a graphic designer to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
      { id: 5, title: 'Project Manager', description: 'We are looking for a project manager to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
      { id: 6, title: 'Software Engineer', description: 'We are looking for a software engineer to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
      { id: 7, title: 'Product Manager', description: 'We are looking for a product manager to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
      { id: 8, title: 'Data Analyst', description: 'We are looking for a data analyst to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
      { id: 9, title: 'Graphic Designer', description: 'We are looking for a graphic designer to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
      { id: 10, title: 'Project Manager', description: 'We are looking for a project manager to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
    ]);
    setExpiredJobs([
      { id: 11, title: 'Software Engineer', description: 'We are looking for a software engineer to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
      { id: 12, title: 'Product Manager', description: 'We are looking for a product manager to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
      { id: 13, title: 'Data Analyst', description: 'We are looking for a data analyst to join our team.', postedAt: '2021-10-01', expiresAt: '2021-11-01'},
    ]);
  }, []);

  return (
    <div className='d-flex'>
    <Sidebar/>
      <div className="jobs-container">
        {/* Active Job Offers */}
        <div className="section">
          <h3>Active Job Offers</h3>
          <div className="card-container">
            {activeJobs.map((job) => (
              <div className="card" key={job.id}>
                <div className="card-body">
                  <h5 className="card-title">
                      {job.title}
                  </h5>
                  <p className="card-text">{job.description}</p>
                  <p className="card-text">posted at: {job.postedAt}</p>
                  <p className="card-text">expires at: {job.expiresAt}</p>
                  <Link to={`/jobs/${job.id}`} className="btn btn-primary card-body-btn">View</Link>
                  <i 
                      className="bi bi-three-dots-vertical card-body-icon"
                      onClick={() => toggleMenu(job.id)}
                  ></i>
                    <div className={`dropdown-menu ${menuVisible === job.id ? 'show' : ''}`}>
                      <button
                          className="dropdown-item"
                          onClick={() => {
                          console.log(`Edit job ${job.id}`);
                          setMenuVisible(null);
                          }}
                      >
                          Edit
                      </button>
                      <button
                          className="dropdown-item red"
                          onClick={() => {
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
        </div>

        {/* Expired Job Offers */}
        <div className="section">
          <h3>Expired Job Offers</h3>
          <div className="card-container">
            {expiredJobs.map((job) => (
              <div className="card" key={job.id}>
                <div className="card-body">
                  <h5 className="card-title">{job.title}</h5>
                  <p className="card-text">{job.description}</p>
                  <p className="card-text">posted at: {job.postedAt}</p>
                  <p className="card-text">expired at: {job.expiresAt}</p>
                  <Link to={`/jobs/${job.id}`} className="btn btn-primary card-body-btn">View</Link>
                      <i 
                          className="bi bi-three-dots-vertical card-body-icon"
                          onClick={() => toggleMenu(job.id)}
                      ></i>
                      <div className={`dropdown-menu small ${menuVisible === job.id ? 'show' : ''}`}>
                      <button
                          className="dropdown-item red"
                          onClick={() => {
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
        </div>
      </div>
    </div>
  );
}

export default Jobs;
