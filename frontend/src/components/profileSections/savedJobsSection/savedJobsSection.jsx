import React, { useEffect, useState } from "react";
import { getSavedJobs } from "../../../services/userProfileService";

export default function SavedJobsSection() {
    const [savedJobs, setSavedJobs] = useState([]);
    const username = document.cookie.split("username=")[1];
    const page = 0;
    const offset = 5;

    useEffect(() => {
        if (!username) return;

        getSavedJobs(username, page, offset)
            .then((data) => {
                setSavedJobs(data);
            })
    },[username])
    return (
        <div className="saved-jobs-section">
            
        </div>
    )
}