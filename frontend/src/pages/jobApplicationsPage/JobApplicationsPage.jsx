import { useParams } from 'react-router-dom';
import { useState, useEffect } from "react";
import './JobApplicationsPage.css';
import { acceptApplication, rejectApplication, getApplications } from '../../services/companyService';

function JobApplicationsPage() {
  const { jobId } = useParams(); 
  const [jobApplicationsData, setJobApplicationsData] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [pageStart, setPageStart] = useState(1); // Start page range
  const [pageEnd, setPageEnd] = useState(5); // End page range
  const applicationsPerPage = 1; // Number of applications per page
  const [successResponse, setSuccessResponse] = useState("");
  const [failureResponse, setFailureResponse] = useState("");
  const handleAccept = async () => {
    console.log(currentPage);
    const response = await acceptApplication(currentPage);
    if (response.success) {
      console.log(response.message);
      setSuccessResponse(response.message);
      jobApplicationsData[currentPage].applicationStatus = 1;
      setFailureResponse("");
    } else {
      setSuccessResponse("");
      setFailureResponse(response.message);
      console.error(response.message);
    }
  };

  const handleReject = async () => {
    console.log(currentPage);
    const response = await rejectApplication(currentPage);
    if (response.success) {
      setSuccessResponse(response.message);
      jobApplicationsData[currentPage].applicationStatus = 2;
      setFailureResponse("");
      console.log(response.message);
    } else {
      setFailureResponse(response.message);
      setSuccessResponse("");
      console.error(response.message);
    }
  };

  useEffect(() => {
    const fetchJobApplications = async () => {
      try {
        const response = await getApplications(jobId);
        setJobApplicationsData(response);
      } catch (error) {
        console.error('Error fetching job applications:', error);
      }
    };
    fetchJobApplications(); 
  }, [jobId]);

  // Pagination logic
  const indexOfLastApplication = currentPage * applicationsPerPage;
  const indexOfFirstApplication = indexOfLastApplication - applicationsPerPage;
  const currentApplications = jobApplicationsData.slice(indexOfFirstApplication, indexOfLastApplication);

  const totalPages = Math.ceil(jobApplicationsData.length / applicationsPerPage);

  // Update page range when the page changes
  const handleNext = () => {
    if (pageEnd < totalPages) {
      setPageStart(pageStart + 5);
      setPageEnd(pageEnd + 5);
    }
  };

  const handlePrevious = () => {
    if (pageStart > 1) {
      setPageStart(pageStart - 5);
      setPageEnd(pageEnd - 5);
    }
  };

  const handlePageChange = (pageNumber) => {
    setCurrentPage(pageNumber);
    if (pageNumber > pageEnd) {
      setPageStart(pageEnd + 1);
      setPageEnd(pageEnd + 5);
    } else if (pageNumber < pageStart) {
      setPageStart(pageStart - 5);
      setPageEnd(pageStart - 1);
    }
  };

  return (
    <div className="applications-container">
      <ul className="applications-list" style={{ listStyleType: 'none' }}>
        {currentApplications.map((application, index) => (
          <li key={index} className="application-card">
            {application.personalData.fullName && (
              <>
                <h3>Personal Information</h3>
                <p><strong>Name:</strong> {application.personalData.fullName}</p>
                <p><strong>Email:</strong> {application.personalData.personalEmail}</p>
                <p><strong>Phone:</strong> {application.personalData.phoneNumber}</p>
                <p><strong>Address:</strong> {application.personalData.address}</p>
                <p><strong>LinkedIn:</strong> <a href={application.personalData.linkedInURL} target="_blank" rel="noopener noreferrer">View Profile</a></p>
              </>
            )}
            {application.educationData.university && (
              <>
                <h3>Education</h3>
                <p><strong>University:</strong> {application.educationData.university}</p>
                <p><strong>Field of Study:</strong> {application.educationData.fieldOfStudy}</p>
                <p><strong>Degree:</strong> {application.educationData.highestDegree}</p>
                <p><strong>Graduation Year:</strong> {application.educationData.graduationYear}</p>
              </>
            )}
            {application.experienceData.length > 0 && (
              <>
                <h3>Experience</h3>
                <ul>
                  {application.experienceData.map((experience, i) => (
                    <li key={i}>
                      <p><strong>Company:</strong> {experience.company}</p>
                      <p><strong>Position:</strong> {experience.position}</p>
                      <p><strong>Start Date:</strong> {experience.startDate}</p>
                      <p><strong>End Date:</strong> {experience.endDate}</p>
                      <p><strong>Description:</strong> {experience.description}</p>
                    </li>
                  ))}
                </ul>
              </>
            )}
            {application.skillData.length > 0 && (
              <>
                <h3>Skills</h3>
                <ul>
                  {application.skillData.map((skill, i) => (
                    <li key={i}>{skill}</li>
                  ))}
                </ul>
              </>
            )}
            {application.specialSectionsData.length > 0 && (
              <>
                {application.specialSectionsData.map((section, sectionIndex) => (
                  <div key={sectionIndex}>
                    <h3>{section.sectionName}</h3>
                    <ul>
                      {Object.entries(section.data).map(([field, value], fieldIndex) => (
                        <li key={fieldIndex}>
                          <strong>{field}:</strong> {Array.isArray(value) ? value.join(', ') : value}
                        </li>
                      ))}
                    </ul>
                  </div>
                ))}
              </>
            )}
            {application.specialFieldsData.length > 0 && (
              <>
                <h3>Special Fields</h3>
                <ul >
                  {application.specialFieldsData.map((field, i) => (
                    <li key={i}><strong>{field.fieldName}:</strong> {field.data}</li>
                  ))}
                </ul>
              </>
            )}
            { application.applicationStatus === 0 &&
            <div className="status-buttons">
                <button className="accept-btn" onClick={handleAccept}>Accept</button>
                <button className="reject-btn" onClick={handleReject}>Reject</button>
                </div>
         }
         {
            application.applicationStatus === 1 && <p className="accepted">Accepted</p>
         }
         {
            application.applicationStatus === 2 && <p className="rejected">Rejected</p>
         }
                <div className="response-message">
                   {successResponse && <p className="success-message">{successResponse}</p>}
                   {failureResponse && <p className="failure-message">{failureResponse}</p>}
                </div>

          </li>
        ))}
      </ul>

      <div className="pagination">
        <button onClick={handlePrevious} disabled={pageStart === 1} className="prev-btn">Prev</button>
        {Array.from({ length: Math.min(5, totalPages - pageStart + 1) }, (_, index) => (
          <button 
            key={pageStart + index}
            onClick={() => handlePageChange(pageStart + index)}
            className={`page-btn ${currentPage === pageStart + index ? 'active' : ''}`}
          >
            {pageStart + index}
          </button>
        ))}
        {pageEnd < totalPages && <span className="dots">...</span>}
        <button onClick={handleNext} disabled={pageEnd >= totalPages} className="next-btn">Next</button>
      </div>
    </div>
  );
}

export default JobApplicationsPage;
