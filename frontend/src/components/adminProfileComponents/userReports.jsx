import React, { useEffect, useState } from 'react';
import UserReportCard from './userReportCard';
import Pagination from './pagination';
import {
    getReportedUsers,
    banApplicant,
    deleteApplicantReport,
} from '../../services/adminServices';

const UserReports = () => {
    const [reports, setReports] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const reportsPerPage = 2;
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        handleUserReports();
    }, [currentPage]);

    const handleUserReports = async () => {
        const { response, totalPages } = await getReportedUsers(currentPage-1, reportsPerPage);
        if (response) {
            setReports(response);
            setTotalPages(totalPages);
        }
    };

    const handleIgnore = async (reportId) => {
        await deleteApplicantReport(reportId);
        await handleUserReports();
    };

    const handleBan = async (reportId) => {
        await banApplicant(reportId);
        await handleUserReports();
    };

    return (
        <div className="reports-container">
            {reports.map(report => (
                <UserReportCard
                    key={report.id}
                    report={report}
                    onIgnore={handleIgnore}
                    onBan={handleBan}
                />
            ))}
            <Pagination
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={setCurrentPage}
            />
        </div>
    );
};

export default UserReports;