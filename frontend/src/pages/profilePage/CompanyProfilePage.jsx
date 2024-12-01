import 'bootstrap/dist/css/bootstrap.min.css';
import { useCallback, useEffect, useState } from 'react';
import './CompanyProfilePage.css';
import Sidebar from '../../components/sideBar/Sidebar';

function CompanyProfilePage(){
    const[companyInfo, setCompanyInfo] = useState({
        company_name: "toto",
        company_email: "toto",
        company_website: "toto",
        company_location: "toto",
        company_overview: "toto",
        company_photo: "/profilePhoto.jpg",
    });
    const[isEditing, setIsEditing] = useState({
        company_name: false,
        company_email: false,
        company_website: false,
        company_location: false,
        company_overview: false,
        company_photo: false,
    });


    const fetchCompanyInfo = useCallback(async() => {
        try{
            const response = await fetch ("http://localhost:3000/company/info", {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            if (response.ok){
                const data = await response.json();
                setCompanyInfo(data);
            }else{
                console.error("Error fetching company info", response.statusText);
            }
        } catch(error) {
            console.error("Error fetching company info", error);
        }
    }, []);

    useEffect(() => {
        fetchCompanyInfo();
    }, [fetchCompanyInfo]);

    const handleEditClick = (field) => {
        setIsEditing((prevMode) => ({
            ...prevMode,
            [field]: true,
        }));
    };

    const handleSaveClick = async(field) => {
        try{
            const response = await fetch("http://localhost:3000/company/info", {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ [field]: companyInfo[field] }),
            });
            if (response.ok){
                setIsEditing((prevMode) => ({
                    ...prevMode,
                    [field]: false,
                }));
                fetchCompanyInfo();
            }else{
                console.error("Error saving company info", response.statusText);
            }
        }catch(error){
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
                company_photo: path,
            }));
        }
    };
    

    return(
        <div className="d-flex">
        <Sidebar />
        <div className='main'>
            <div className="mb-3 profile-container">
                <div className="position-relative">
                    <img
                        src={companyInfo.company_photo}
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
                    {isEditing.company_name ? (
                        <div className="d-flex align-items-center">
                            <input
                                type="text"
                                className="form-control name"
                                value={companyInfo.company_name}
                                onChange={(e) => handleInputChange('company_name', e.target.value)}
                            />
                            <button
                                className="btn btn-success"
                                onClick={() => handleSaveClick('company_name')}
                            >
                                Save
                            </button>
                        </div>
                    ) : (
                        <h3
                            className="text-center editable-name"
                            onClick={() => handleEditClick('company_name')}
                        >
                            {companyInfo.company_name}
                        </h3>
                    )}
                </div>
            </div>


            <div className="mb-3 box">
                <label className="form-label">Email</label>
                {isEditing.company_email ? (
                    <div className="d-flex">
                        <input
                            type="text"
                            className="form-control me-2"
                            value={companyInfo.company_email}
                            onChange={(e) => handleInputChange('company_email', e.target.value)}
                        />
                        <button
                            className="btn btn-success"
                            onClick={() => handleSaveClick('company_email')}
                        >
                            Save
                        </button>
                    </div>
                ) : (
                    <div className="d-flex align-items-center">
                        <h3 className="me-2">{companyInfo.company_email}</h3>
                        <button
                            className="btn btn-primary"
                            onClick={() => handleEditClick('company_email')}
                        >
                            Edit
                        </button>
                    </div>
                )}
            </div>

            <div className="mb-3 box">
                <label className="form-label">Website</label>
                {isEditing.company_website ? (
                    <div className="d-flex">
                        <input
                            type="text"
                            className="form-control me-2"
                            value={companyInfo.company_website}
                            onChange={(e) => handleInputChange('company_website', e.target.value)}
                        />
                        <button
                            className="btn btn-success"
                            onClick={() => handleSaveClick('company_website')}
                        >
                            Save
                        </button>
                    </div>
                ) : (
                    <div className="d-flex align-items-center">
                        <h3 className="me-2">{companyInfo.company_website}</h3>
                        <button
                            className="btn btn-primary"
                            onClick={() => handleEditClick('company_website')}
                        >
                            Edit
                        </button>
                    </div>
                )}
            </div>

            <div className="mb-3 box">
                <label className="form-label">Location</label>
                {isEditing.company_location ? (
                    <div className="d-flex">
                        <input
                            type="text"
                            className="form-control me-2"
                            value={companyInfo.company_location}
                            onChange={(e) => handleInputChange('company_location', e.target.value)}
                        />
                        <button
                            className="btn btn-success"
                            onClick={() => handleSaveClick('company_location')}
                        >
                            Save
                        </button>
                    </div>
                ) : (
                    <div className="d-flex align-items-center">
                        <h3 className="me-2">{companyInfo.company_location}</h3>
                        <button
                            className="btn btn-primary"
                            onClick={() => handleEditClick('company_location')}
                        >
                            Edit
                        </button>
                    </div>
                )}
            </div>

            <div className="mb-3 box">
                <label className="form-label">Overview</label>
                {isEditing.company_overview ? (
                    <div className="d-flex">
                        <textarea
                            className="form-control me-2"
                            value={companyInfo.company_overview}
                            onChange={(e) => handleInputChange('company_overview', e.target.value)}
                        />
                        <button
                            className="btn btn-success"
                            onClick={() => handleSaveClick('company_overview')}
                        >
                            Save
                        </button>
                    </div>
                ) : (
                    <div className="d-flex align-items-center">
                        <h3 className="me-2">{companyInfo.company_overview}</h3>
                        <button
                            className="btn btn-primary"
                            onClick={() => handleEditClick('company_overview')}
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

