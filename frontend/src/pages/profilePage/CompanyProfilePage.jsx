import 'bootstrap/dist/css/bootstrap.min.css';
import { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import './CompanyProfilePage.css';
import Sidebar from '../../components/sideBar/Sidebar';
import CreateJob from '../../components/CreateJob/createJob';

function CompanyProfilePage() {
    const { companyUsername } = useParams();
    // const history = useHistory();

    const [companyInfo, setCompanyInfo] = useState({
        name: "company name",
        email: "company email",
        website: "company website",
        location: "company location",
        overview: "company overview",
        photo: "/profilePhoto.jpg",
    });
    const [isEditing, setIsEditing] = useState({
        name: false,
        website: false,
        location: false,
        overview: false,
        photo: false,
    });
    const [showCreateJob, setShowCreateJob] = useState(false); // New state variable

    const fetchCompanyInfo = useCallback(async () => {
        try {
            const response = await fetch(`http://localhost:8080/company/${companyUsername}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
            });
            if (response.ok) {
                const data = await response.json();
                console.log(data);
                setCompanyInfo(data);
            } else {
                console.error("Error fetching company info", response.statusText);
            }
        } catch (error) {
            console.error("Error fetching company info", error);
        }
    }, [companyUsername]);

    useEffect(() => {
        fetchCompanyInfo();
    }, [fetchCompanyInfo]);

    const handleEditClick = (field) => {
        setIsEditing((prevMode) => ({
            ...prevMode,
            [field]: true,
        }));
    };

    const handleSaveClick = async (field) => {
        try {
            const response = await fetch(`http://localhost:8080/company/${companyUsername}`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ [field]: companyInfo[field] }),
            });
            if (response.ok) {
                setIsEditing((prevMode) => ({
                    ...prevMode,
                    [field]: false,
                }));
                fetchCompanyInfo();
            } else {
                console.error("Error saving company info", response.statusText);
            }
        } catch (error) {
            console.error("Error saving company info", error);
        }
    }

    const handleInputChange = (field, value) => {
        setCompanyInfo((prevInfo) => ({
            ...prevInfo,
            [field]: value,
        }));
    }

    const handlePhotoChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            const path = URL.createObjectURL(file);
            setCompanyInfo((prevInfo) => ({
                ...prevInfo,
                photo: path,
            }));
        }
    };

    const handleCreateJob = () => {
        setShowCreateJob(true); // Show the CreateJob component
    }

    const handleCloseModal = () => {
        setShowCreateJob(false); // Hide the CreateJob component
    }

    // const logout = () => {
    //     localStorage.removeItem('auth_token');
    //     // redirect to the login page
    //     history.push('/login');
    // };

    return (
        <div className="d-flex">
            <Sidebar />
            <div className='main'>
                <div className="mb-3 profile-container">
                    <div className="position-relative">
                        <img
                            src={companyInfo.photo}
                            alt="Profile"
                            className="profile-photo"
                        />
                        <i
                            className="bi bi-camera-fill change-photo-icon"
                            onClick={() => document.getElementById('file-input').click()}
                            title="Change Profile Photo"
                        ></i>
                        <input
                            id="file-input"
                            type="file"
                            accept="image/*"
                            style={{ display: 'none' }}
                            onChange={handlePhotoChange}
                        />
                    </div>
                    <div className="profile-name">
                        {isEditing.name ? (
                            <div className="d-flex align-items-center">
                                <input
                                    type="text"
                                    className="form-control name"
                                    value={companyInfo.name}
                                    onChange={(e) => handleInputChange('name', e.target.value)}
                                />
                                <button
                                    className="btn btn-success"
                                    onClick={() => handleSaveClick('name')}
                                >
                                    Save
                                </button>
                            </div>
                        ) : (
                            <h3
                                className="text-center editable-name"
                                onClick={() => handleEditClick('name')}
                            >
                                {companyInfo.name}
                            </h3>
                        )}
                    </div>
                </div>

                <div className="mb-3 box">
                    <label className="form-label">Email</label>
                    <div className="d-flex align-items-center">
                        <h3 className="me-2">{companyInfo.email}</h3>
                    </div>
                </div>

                <div className="mb-3 box">
                    <label className="form-label">Website</label>
                    {isEditing.website ? (
                        <div className="d-flex">
                            <input
                                type="text"
                                className="form-control me-2"
                                value={companyInfo.website}
                                onChange={(e) => handleInputChange('website', e.target.value)}
                            />
                            <button
                                className="btn btn-success"
                                onClick={() => handleSaveClick('website')}
                            >
                                Save
                            </button>
                        </div>
                    ) : (
                        <div className="d-flex align-items-center">
                            <h3 className="me-2">{companyInfo.website}</h3>
                            <button
                                className="btn btn-primary"
                                onClick={() => handleEditClick('website')}
                            >
                                Edit
                            </button>
                        </div>
                    )}
                </div>

                <div className="mb-3 box">
                    <label className="form-label">Location</label>
                    {isEditing.location ? (
                        <div className="d-flex">
                            <input
                                type="text"
                                className="form-control me-2"
                                value={companyInfo.location}
                                onChange={(e) => handleInputChange('location', e.target.value)}
                            />
                            <button
                                className="btn btn-success"
                                onClick={() => handleSaveClick('location')}
                            >
                                Save
                            </button>
                        </div>
                    ) : (
                        <div className="d-flex align-items-center">
                            <h3 className="me-2">{companyInfo.location}</h3>
                            <button
                                className="btn btn-primary"
                                onClick={() => handleEditClick('location')}
                            >
                                Edit
                            </button>
                        </div>
                    )}
                </div>

                <div className="mb-3 box">
                    <label className="form-label">Overview</label>
                    {isEditing.overview ? (
                        <div className="d-flex">
                            <textarea
                                className="form-control me-2"
                                value={companyInfo.overview}
                                onChange={(e) => handleInputChange('overview', e.target.value)}
                            />
                            <button
                                className="btn btn-success"
                                onClick={() => handleSaveClick('overview')}
                            >
                                Save
                            </button>
                        </div>
                    ) : (
                        <div className="d-flex align-items-center">
                            <h3 className="me-2">{companyInfo.overview}</h3>
                            <button
                                className="btn btn-primary"
                                onClick={() => handleEditClick('overview')}
                            >
                                Edit
                            </button>
                        </div>
                    )}
                </div>

                <button className="btn btn-primary custom-btn" onClick={handleCreateJob}>Create New Job</button>

                {/* Modal */}
                {showCreateJob && (
                <div className="modal show d-block" tabIndex="-1">
                    <div className="modal-dialog custom-modal-size"> {/* Custom class */}
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Create Job</h5>
                                {/* <button type="button" className="btn-close" onClick={handleCloseModal}></button> */}
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
        </div>
    );
}

export default CompanyProfilePage;