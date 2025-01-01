import React, { useState } from 'react';
import Modal from './modal';
import JobDetails from './jobDetails';
import './styles/reportCard.css';

const ReportCard = ({ report, onIgnore, onDelete }) => {
    const [isModalOpen, setIsModalOpen] = useState(false);

    const formattedDate = (date)=> { 
        return new Date(date).toLocaleString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: 'numeric',
            minute: 'numeric',
            hour12: true
        });
    }
    return (
        <div className="report-card">
            <div className="report-header">
                <h3>{report.job.title}</h3>
                <span className="report-date">{formattedDate(report.createdAt)}</span>
            </div>
            <div className="report-content">
                <p><strong>Reported by:</strong> {report.applicant.firstName} {report.applicant.lastName}</p>
                <p><strong>Reason:</strong> {report.jobReportReason}</p>
                <p><strong>Description:</strong> {report.reportDescription}</p>
                <p><strong>Job ID:</strong> {report.job.id}</p>
            </div>
            <div className="report-actions">
                <button className="btn-view" onClick={() => setIsModalOpen(true)}>
                    View Details
                </button>
                <button className="btn-ignore" onClick={() => onIgnore(report.id)}>
                    Ignore Report
                </button>
                <button className="btn-delete" onClick={() => onDelete(report.job.id)}>
                    Delete Job
                </button>
            </div>

            <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <JobDetails job={report.job} />
            </Modal>
        </div>
    );
};

export default ReportCard;
