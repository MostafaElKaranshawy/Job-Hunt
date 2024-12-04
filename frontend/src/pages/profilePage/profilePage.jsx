import React, { useState, useEffect } from "react";
import { NavLink, useParams } from "react-router-dom";
import Header from '../../components/header/header';
import ProfileSectionNavigator from "../../components/profileSections/profileSectionsNavigator/profileSectionsNavigator";
import ConfirmationBox from "../../components/confirmationBox/confirmationBox.";
import "./profilePage.css";

export default function ProfilePage() {
    const { userName, profileSection } = useParams();
    const currentSection = profileSection || '';
    const [showConfirmationBox, setShowConfirmationBox] = useState(false);
    useEffect(() => {
        console.log("showConfirmationBox", showConfirmationBox);
    }, [showConfirmationBox]);

    const logOut = () => {
        setShowConfirmationBox(false);
        window.location.href = "/";
    };

    const cancelLogOut = () => {
        setShowConfirmationBox(false);
    };

    return (
        <div className="profile-page">
            <Header />
            {showConfirmationBox && (
                <ConfirmationBox 
                    content={"Do you want to log out?"} 
                    confirm={logOut} 
                    cancel={cancelLogOut} 
                />
            )}
            <div className="main-profile">
                <div className="profile-nav">
                    <NavLink to={`/user/${userName}/profile/profileInfo`} className='profile-section'>
                        <i className="fa-solid fa-user-tie"></i>
                        <p>Profile Info</p>
                    </NavLink>
                    <NavLink to={`/user/${userName}/profile/savedJobs`} className='profile-section'>
                        <i className="fa-solid fa-bookmark"></i>
                        <p>Saved Jobs</p>
                    </NavLink>
                    <NavLink to={`/user/${userName}/profile/jobsApplications`} className='profile-section'>
                        <i className="fa-solid fa-window-maximize"></i>
                        <p>Jobs Applications</p>
                    </NavLink>
                    <NavLink to={`/user/${userName}/profile/settings`} className='profile-section'>
                        <i className="fa-solid fa-gear"></i>
                        <p>Settings</p>
                    </NavLink>
                    <div className='profile-section' onClick={() => {
                        console.log("Logout");
                        setShowConfirmationBox(true)
                    }}>
                        <i className="fa-solid fa-right-from-bracket"></i>
                        <p>Logout</p>
                    </div>
                </div>
                <div className="nav-section">
                    <ProfileSectionNavigator profileSection={currentSection} />
                </div>
            </div>
        </div>
    );
}
