import React, { useEffect, useState } from 'react';
import Navigation from '../../components/adminProfileComponents/navigation';
import ReportCard from '../../components/adminProfileComponents/reportCard';
import UserReports from '../../components/adminProfileComponents/userReports.jsx';
import Pagination from '../../components/adminProfileComponents/pagination';
import './adminDashboard.css';

import { getReportedJobs, deleteJobReport, deleteReportedJob } from '../../services/adminServices.js';

const AdminDashboard = () => {
    const [activeTab, setActiveTab] = useState('jobs');
    const [jobReports, setJobReports] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const reportsPerPage = 2;

    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        handleGetReportedJobs();
    }, [currentPage])

    useEffect(() => {
        if(activeTab === 'jobs')
            handleGetReportedJobs();
    }, [activeTab])

    const handleGetReportedJobs = async () => {
        try {
            const { response, totalPages } = await getReportedJobs(currentPage-1, reportsPerPage);
            console.log(response)
            setJobReports(response);
            setTotalPages(totalPages);
        }
        catch (e) {
            console.log(e.message)
        }
    }


    const handleIgnore = async (reportId) => {
        try {
            await deleteJobReport(reportId);
            await handleGetReportedJobs();
        }
        catch (e) {
            console.log(e.message)
        }
    };

    const handleDelete = async (reportId) => {
        try {
            await deleteReportedJob(reportId);
            await handleGetReportedJobs();
        }
        catch (e) {
            console.log(e.message)
        }
    };

    return (
        <div className="admin-dashboard">
            <h1>Reports Management</h1>
            <Navigation activeTab={activeTab} onTabChange={setActiveTab} />

            {activeTab === 'jobs' ? (
                <div className="reports-container">
                    {jobReports.map(report => (
                        <ReportCard
                            key={report.id}
                            report={report}
                            onIgnore={handleIgnore}
                            onDelete={handleDelete}
                        />
                    ))}
                    <Pagination
                        currentPage={currentPage}
                        totalPages={totalPages}
                        onPageChange={setCurrentPage}
                    />
                </div>
            ) : (
                <UserReports />
            )}
        </div>
    );
};

export default AdminDashboard;