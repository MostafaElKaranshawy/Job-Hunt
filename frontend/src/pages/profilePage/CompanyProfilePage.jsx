import 'bootstrap/dist/css/bootstrap.min.css';
import { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import './CompanyProfilePage.css';
import Sidebar from '../../components/sideBar/Sidebar';
const backendURL = import.meta.env.VITE_BACKEND_URL;


function CompanyProfilePage() {
    const { companyUsername } = useParams();

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

    const fetchCompanyInfo = useCallback(async () => {
        try {
            const response = await fetch(`${backendURL}/company/${companyUsername}`, {
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
            const response = await fetch(`${backendURL}/company/${companyUsername}`, {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
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
                                className="btn edit-btn-primary"
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
                                className="btn edit-btn-primary"
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
                                className="btn edit-btn-primary"
                                onClick={() => handleEditClick('overview')}
                            >
                                Edit
                            </button>
                        </div>
                    )}
                </div>

                
            </div>
        </div>
    );
}

export default CompanyProfilePage;