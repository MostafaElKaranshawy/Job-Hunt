import React, { useState } from 'react';
import Modal from './modal';
import UserDetails from './userDetails';
import './styles/reportCard.css';

const UserReportCard = ({ report, onIgnore, onBan }) => {
    const [isModalOpen, setIsModalOpen] = useState(false);

    return (
        <div className="report-card">
            <div className="report-header">
                <h3>{report.applicant.firstName} {report.applicant.lastName || "Unknown User"}</h3>
                <span className="report-date">{new Date(report.applicant.createdAt).toLocaleDateString()}</span>
            </div>
            <div className="report-content">
                <p><strong>Reason:</strong> {report.applicantReportReason}</p>
                <p><strong>Description:</strong> {report.description || "No description provided."}</p>
                <p><strong>User ID:</strong> {report.applicant.id}</p>
            </div>
            <div className="report-actions">
                <button className="btn-view" onClick={() => setIsModalOpen(true)}>
                    View Details
                </button>
                <button className="btn-ignore" onClick={() => onIgnore(report.applicant.id)}>
                    Ignore Report
                </button>
                <button className="btn-delete" onClick={() => onBan(report.applicant.id)}>
                    Ban User
                </button>
            </div>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <UserDetails user={report.applicant} />
            </Modal>
        </div>
    );
};

export default UserReportCard;
