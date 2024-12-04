import React from "react";
import ProfileInfoSection from "../profileInfoSection/profileInfoSection";
import SavedJobsSection from "../savedJobsSection/savedJobsSection";
import JobsApplicationSection from "../jobsApplicationsSection/jobsApplicationSection";
import ProfileSettings from "../profileSettings/profileSettings";
import "./profileSectionsNavigator.css";
export default function ProfileSectionNavigator({profileSection}) {
    return (
        <div className="profile-section-nav">
            {
                (profileSection == 'profileInfo')?<ProfileInfoSection/>
                :(profileSection == 'savedJobs')?<SavedJobsSection/>
                :(profileSection == 'jobsApplications')?<JobsApplicationSection/>
                :(profileSection == 'settings')?<ProfileSettings/>
                :<></>
            }
        </div>
    )
}