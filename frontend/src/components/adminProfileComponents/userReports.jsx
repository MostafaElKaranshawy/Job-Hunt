import React, { useEffect, useState } from 'react';
import UserReportCard from './userReportCard';
import Pagination from './pagination';
import ConfirmationBox from '../confirmationBox/confirmationBox.jsx';
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
    const [showConfirmation, setShowConfirmation] = useState(false);
    const [confirmationBody, setConfirmationBody] = useState({});

    useEffect(() => {
        handleUserReports();
    }, [currentPage]);

    const handleShowConfirmation = (content, onConfirm) => {
        setConfirmationBody({
            content,
            confirm: async () => {
                await onConfirm();
                setShowConfirmation(false);
            },
            cancel: () => {
                setShowConfirmation(false);
            },
        });
        setShowConfirmation(true);
    };

    const handleUserReports = async () => {
        try {
            const { response, totalPages } = await getReportedUsers(currentPage - 1, reportsPerPage);
            if (response) {
                setReports(response);
                setTotalPages(totalPages);
            }
        } catch (e) {
            console.log(e.message);
        }
    };

    const handleConfirmIgnore = (reportId) => {
        handleShowConfirmation("Are you sure you want to ignore this report?", () => handleIgnore(reportId));
    };

    const handleConfirmBan = (reportId) => {
        handleShowConfirmation("Are you sure you want to ban this user?", () => handleBan(reportId));
    };

    const handleIgnore = async (reportId) => {
        try {
            await deleteApplicantReport(reportId);
            await handleUserReports();
        } catch (e) {
            console.log(e.message);
        }
    };

    const handleBan = async (reportId) => {
        try {
            console.log(reportId)
            await banApplicant(reportId);
            await handleUserReports();
        } catch (e) {
            console.log(e.message);
        }
    };

    return (
        <div className="reports-container">
            {showConfirmation && (
                <ConfirmationBox
                    content={confirmationBody.content}
                    confirm={confirmationBody.confirm}
                    cancel={confirmationBody.cancel}
                />
            )}
            {reports.map((report) => (
                <UserReportCard
                    key={report.id}
                    report={report}
                    onIgnore={() => handleConfirmIgnore(report.id)}
                    onBan={() => handleConfirmBan(report.applicant.id)}
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
