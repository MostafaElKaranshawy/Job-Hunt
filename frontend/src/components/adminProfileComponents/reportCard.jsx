import React, { useState } from 'react';
import Modal from './modal';
import JobDetails from './jobDetails';
import './styles/reportCard.css';


const ReportCard = ({ report, onIgnore, onDelete }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <div className="report-card">
      <div className="report-header">
        <h3>{report.jobTitle}</h3>
        <span className="report-date">{new Date(report.reportDate).toLocaleDateString()}</span>
      </div>
      <div className="report-content">
        <p><strong>Reported by:</strong> {report.reportedBy}</p>
        <p><strong>Reason:</strong> {report.reason}</p>
        <p><strong>Job ID:</strong> {report.jobId}</p>
      </div>
      <div className="report-actions">
        <button className="btn-view" onClick={() => setIsModalOpen(true)}>
          View Details
        </button>
        <button className="btn-ignore" onClick={() => onIgnore(report.id)}>
          Ignore Report
        </button>
        <button className="btn-delete" onClick={() => onDelete(report.id)}>
          Delete Job
        </button>
      </div>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
        <JobDetails job={report} />
      </Modal>
    </div>
  );
}

export default ReportCard;