const apiUrl = 'http://localhost:8080';

async function getReportedJobs(page, offset) {
    const response = await fetch(`${apiUrl}/admin/reported-jobs?page=${page}&offset=${offset}`, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },

    });
    // console.log(response);
    const data = await response.json();
    
    return {response: data.content, totalPages: data.totalPages};
}

async function getReportedUsers(page, offset) {
    const response = await fetch(`${apiUrl}/admin/reported-applicants?page=${page}&offset=${offset}`, {
        method: 'GET',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },

    });
    const data = await response.json();
    console.log(data.content);

    return {response: data.content, totalPages: data.totalPages};
}

async function banApplicant(applicantId) {
    const response = await fetch(`${apiUrl}/admin/reported-applicants/${applicantId}/ban`, {
        method: 'PATCH',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },

    });
    
    if (response.status === 200) {
        return true;
    }
    return false;
}

async function deleteJobReport(jobReportId) {
    const response = await fetch(`${apiUrl}/admin/reported-jobs/${jobReportId}/report`, {
        method: 'DELETE',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },

    });
    
    if (response.status === 200) {
        return true;
    }
    return false;
}

async function deleteApplicantReport(applicantReportId) {
    const response = await fetch(`${apiUrl}/admin/reported-applicants/${applicantReportId}/report`, {
        method: 'DELETE',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },

    });
    
    if (response.status === 200) {
        return true;
    }
    return false;
}

async function deleteReportedJob(jobId) {
    const response = await fetch(`${apiUrl}/admin/reported-jobs/${jobId}`, {
        method: 'DELETE',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
        },

    });
    
    if (response.status === 200) {
        return true;
    }
    return false;
}

export {
    getReportedJobs,
    getReportedUsers,
    banApplicant,
    deleteJobReport,
    deleteApplicantReport,
    deleteReportedJob
};