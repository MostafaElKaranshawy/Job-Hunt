import { useParams } from 'react-router-dom';
import { useState, useEffect } from "react";
import './JobApplicationsPage.css';
import { acceptApplication, rejectApplication } from '../../services/companyService';

function JobApplicationsPage() {
  const { jobId } = useParams(); 
  const [jobApplicationsData, setJobApplicationsData] = useState([]);
  const [expandedIndex, setExpandedIndex] = useState(null); // To track expanded application

  const getApplications = async () => {
    try {
      const companyUsername = 'company1'; // Replace with actual company username
      const jobId = 2; // Replace with actual job ID
  
      const response = await fetch(
        `http://localhost:8080/company/${companyUsername}/jobs/${jobId}`,
        {
          credentials: "include", // Include credentials for cookies/auth
        }
      );
  
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
  
      const data = await response.json();
      console.log("Job applications data:", data);
      setJobApplicationsData(data); // Assuming setJobApplicationsData is defined
    } catch (error) {
      console.error("Error fetching job applications:", error);
    }
  };

  const handleAccept = async (index) => {
    console.log(index);
    const response = await acceptApplication( index );
    if (response.success) {
      console.log(response.message);
      // Handle success
    } else {
      console.error(response.message);
      // Handle error
    }
  };

  const handleReject = async (index) => {
    const response = await rejectApplication( index );
    if (response.success) {
      console.log(response.message);
      // Handle success
    } else {
      console.error(response.message);
      // Handle error
    }
  };

  

  useEffect(() => {
    const fetchJobApplications = async () => {
        try {
            // Using the provided response as mock data
            const response = [
                {
                    educationData: {
                        fieldOfStudy: "Computer Engineering",
                        graduationYear: "2026",
                        highestDegree: "Bachelor of Engineering (BE)",
                        startYear: "2021",
                        university: "Alexandria University"
                    },
                    experienceData: {
                        companyName: "Cellula",
                        currentRule: false,
                        endDate: "2025-05-10",
                        jobDescription: "jndfenew\nwefd;new;kd\ndjfj",
                        jobLocation: "onsite",
                        jobTitle: "ML ENG",
                        startDate: "2024-12-01"
                    },
                    personalData: {
                        address: "Alexandria, Sporting, Port said street",
                        dateOfBirth: "2025-01-01",
                        fullName: "Mustafa Kamel",
                        linkedInURL: "https://linkedin.com/in/mostafa-nofal-923103197/",
                        personalEmail: "mstfnfl@gmail.com",
                        phoneNumber: "201099100787",
                        portfolioURL: "https://linkedin.com/in/mostafa-nofal-923103197/"
                    },
                    skillData: ["Java", "Frontend Development/Scripting", "Javascript"],
                    specialFieldsData: [
                        { fieldName: "custom field 1", data: "2025-01-01" },
                        { fieldName: "custom field 2", data: "x3" },
                        { fieldName: "custom field 3", data: "lksfldkfdsselknfs" }
                    ],
                    specialSectionsData: [
                        {
                            sectionName: "Custom section 1",
                            data: {
                                "field 1": "kjrgfrkjf",
                                "field 2": ",mrfrm,\nslfkner;k",
                                "field 3": " option 2"
                            }
                        },
                        {
                            sectionName: "custom section 2",
                            data: {
                                "field 1 (checkbox)": ["y", "x", "z", "n"],
                                "field 2": "fndlkdsfnlwkejf"
                            }
                        }
                    ]
                },
				{
					educationData: {
						fieldOfStudy: "Computer Engineering",
						graduationYear: "2026",
						highestDegree: "Bachelor of Engineering (BE)",
						startYear: "2021",
						university: "Alexandria University"
					},
					experienceData: {
						companyName: "Cellula",
						currentRule: false,
						endDate: "2025-05-10",
						jobDescription: "jndfenew\nwefd;new;kd\ndjfj",
						jobLocation: "onsite",
						jobTitle: "ML ENG",
						startDate: "2024-12-01"
					},
					personalData: {
						address: "Alexandria, Sporting, Port said street",
						dateOfBirth: "2025-01-01",
						fullName: "Mustafa Kamel",
						linkedInURL: "https://linkedin.com/in/mostafa-nofal-923103197/",
						personalEmail: "yomna@mail.com",
						phoneNumber: "201099100787",
						portfolioURL: "https://linkedin.com/in/mostafa-nofal-923103197/"
					},
					skillData: ["Java", "Frontend Development/Scripting", "Javascript"],
					specialFieldsData: [
						{ fieldName: "custom field 1", data: "2025-01-01" },
						{ fieldName: "custom field 2", data: "x3" },
						{ fieldName: "custom field 3", data: "lksfldkfdsselknfs" }
					],
					specialSectionsData: [
						{
							sectionName: "Custom section 1",
							data: {
								"field 1": "kjrgfrkjf",
								"field 2": ",mrfrm,\nslfkner;k",
								"field 3": " option 2"
							}
						},
						{
							sectionName: "custom section 2",
							data: {
								"field 1 (checkbox)": ["y", "x", "z", "n"],
								"field 2": "fndlkdsfnlwkejf"
							}
						}
					]
				},
            ];
            setJobApplicationsData(response);
        } catch (error) {
            console.error('Error fetching job applications:', error);
        }
    };

    fetchJobApplications(); 
  }, []);

  // Toggle expanded application
  const toggleExpand = (index) => {
    setExpandedIndex(expandedIndex === index ? null : index);
  };

  return (
    <div className="applications-container">
      <ul>
        {jobApplicationsData.map((application, index) => (
          <li key={index} className="application-card">
            <h3>Personal Data</h3>
            <p><strong>Name:</strong> {application.personalData.fullName}</p>
            <p><strong>Email:</strong> {application.personalData.personalEmail}</p>
            <p><strong>Phone:</strong> {application.personalData.phoneNumber}</p>
            {expandedIndex === index && (
              <>
                <p><strong>Address:</strong> {application.personalData.address}</p>
                <p><strong>LinkedIn:</strong> <a href={application.personalData.linkedInURL} target="_blank" rel="noopener noreferrer">View Profile</a></p>

                <h3>Education</h3>
                <p><strong>University:</strong> {application.educationData.university}</p>
                <p><strong>Field of Study:</strong> {application.educationData.fieldOfStudy}</p>
                <p><strong>Degree:</strong> {application.educationData.highestDegree}</p>
                <p><strong>Graduation Year:</strong> {application.educationData.graduationYear}</p>

                <h3>Experience</h3>
                <p><strong>Company:</strong> {application.experienceData.companyName}</p>
                <p><strong>Title:</strong> {application.experienceData.jobTitle}</p>
                <p><strong>Description:</strong> {application.experienceData.jobDescription}</p>

                <h3>Skills</h3>
                <ul>
                  {application.skillData.map((skill, i) => (
                    <li key={i}>{skill}</li>
                  ))}
                </ul>

                <h3>Special Fields</h3>
                <ul>
                  {application.specialFieldsData.map((field, i) => (
                    <li key={i}><strong>{field.fieldName}:</strong> {field.data}</li>
                  ))}
                </ul>

                <h3>Special Sections</h3>
                {application.specialSectionsData?.map((section, sectionIndex) => (
                    <div key={sectionIndex}>
                        <h4>{section.sectionName}</h4>
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
            <button className="read-more-btn" onClick={() => toggleExpand(index)}>
              {expandedIndex === index ? "Show Less" : "Read More"}
            </button>
            {/* <select name="status" id="status">
          <option value="pending">Under Review</option>
          <option value="approved">Accepted</option>
          <option value="rejected">Rejected</option>
        </select> */}
            <button className="accept-btn" onClick={()=>handleAccept(index)}>Accept</button>
            <button className="reject-btn" onClick={()=>handleReject(index)}>Reject</button>
          </li>
        ))}

      </ul>
      <button onClick={getApplications}>click</button>
    </div>
  );
}
export default JobApplicationsPage;
