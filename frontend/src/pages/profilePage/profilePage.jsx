import React, {} from "react";
import { NavLink, useParams } from "react-router-dom";
import Header from '../../components/header/header';
import ProfileSectionNavigator from "../../components/profileSections/profileSectionsNavigator/profileSectionsNavigator";

import "./profilePage.css";

export default function ProfilePage() {
    const { userName, profileSection } = useParams();
    const currentSection = profileSection || 'profileInfo';
    console.log(userName)
    return (
        <div className="profile-page">
            <Header/>
            <div className="main-profile">
                <div className="profile-nav">
                    <NavLink to={`/${userName}/profile/profileInfo`} className='profile-section'>
                        <i></i>
                        <p>
                            Profile Info
                        </p>
                    </NavLink>
                    <NavLink to={`/${userName}/profile/savedJobs`} className='profile-section'>
                        <i></i>
                        <p>
                            Saved Jobs
                        </p>
                    </NavLink>
                    <NavLink to={`/${userName}/profile/jobsApplications`} className='profile-section'>
                        <i></i>
                        <p>
                            Jobs Applications
                        </p>
                    </NavLink>
                    <NavLink to={`/${userName}/profile/settings`} className='profile-section'>
                        <i></i>
                        <p>
                            Settings
                        </p>
                    </NavLink>
                    <div className='profile-section'>
                        <i></i>
                        <p>
                            Logout
                        </p>
                    </div>
                </div>
                <div className="nav-section">
                    <ProfileSectionNavigator profileSection={currentSection}/>
                </div>
            </div>
        </div>
    )
}