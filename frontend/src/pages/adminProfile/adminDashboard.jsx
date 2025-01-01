import React, { useEffect, useState } from 'react';
import Navigation from '../../components/adminProfileComponents/navigation';
import ReportCard from '../../components/adminProfileComponents/reportCard';
import UserReports from '../../components/adminProfileComponents/userReports.jsx';
import Pagination from '../../components/adminProfileComponents/pagination';
import ConfirmationBox from '../../components/confirmationBox/confirmationBox.jsx';
import './adminDashboard.css';

import { getReportedJobs, deleteJobReport, deleteReportedJob } from '../../services/adminServices.js';

const AdminDashboard = () => {
    const [showConfirmation, setShowConfirmation] = useState(false);
    const [confirmationBody, setConfirmationBody] = useState(null);
    const [activeTab, setActiveTab] = useState('jobs');
    const [jobReports, setJobReports] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const reportsPerPage = 2;
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        handleGetReportedJobs();
    }, [currentPage]);

    useEffect(() => {
        if (activeTab === 'jobs') 
            handleGetReportedJobs();
    }, [activeTab]);

    useEffect(() => {
        if (confirmationBody) setShowConfirmation(true);
    }, [confirmationBody]);

    const handleShowConfirmation = (content, confirm) => {
        setConfirmationBody({
            content,
            confirm: async () => {
                await confirm();
                setShowConfirmation(false);
            },
            cancel: () => {
                setShowConfirmation(false);
            },
        });
    };

    const handleGetReportedJobs = async () => {
        try {
            const { response, totalPages } = await getReportedJobs(currentPage - 1, reportsPerPage);
            setJobReports(response);
            setTotalPages(totalPages);
            
        } catch (e) {
            console.log(e.message);
        }
    };

    const handleConfirmIgnore = (reportId) => {
        handleShowConfirmation("Are you sure you want to ignore this report?", () => handleIgnore(reportId));
    };

    const handleConfirmDelete = (reportId) => {
        handleShowConfirmation("Are you sure you want to delete this report?", () => handleDelete(reportId));
    };

    const handleIgnore = async (reportId) => {
        try {
            await deleteJobReport(reportId);
            await handleGetReportedJobs();
        } catch (e) {
            console.log(e.message);
        }
    };

    const handleDelete = async (reportId) => {
        try {
            await deleteReportedJob(reportId);
            await handleGetReportedJobs();
        } catch (e) {
            console.log(e.message);
        }
    };

    return (
        <div className="admin-dashboard">
            {showConfirmation && (
                <ConfirmationBox
                    {...confirmationBody}
                />
            )}
            <h1>Reports Management</h1>
            <Navigation activeTab={activeTab} onTabChange={setActiveTab} />

            {activeTab === 'jobs' ? (
                <div className="reports-container">
                    {jobReports.map((report) => (
                        <ReportCard
                            key={report.id}
                            report={report}
                            onIgnore={handleConfirmIgnore}
                            onDelete={handleConfirmDelete}
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
