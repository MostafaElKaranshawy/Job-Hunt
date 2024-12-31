import React, { useState } from 'react';
import Navigation from '../../components/adminProfileComponents/navigation';
import ReportCard from '../../components/adminProfileComponents/reportCard';
import UserReports from '../../components/adminProfileComponents/userReports.jsx';
import Pagination from '../../components/adminProfileComponents/pagination';
import './adminDashboard.css';

// Mock data for job reports
const mockJobReports = [
  {
    id: 1,
    jobId: "JOB-001",
    jobTitle: "Software Engineer",
    company: "Tech Corp",
    location: "New York, NY",
    salary: "$120,000 - $150,000",
    description: "We are looking for a skilled software engineer to join our team...",
    reportedBy: "John Doe",
    reason: "Job posting seems fraudulent",
    reportDate: "2024-03-10"
  }
];

const AdminDashboard = () => {
  const [activeTab, setActiveTab] = useState('jobs');
  const [jobReports, setJobReports] = useState(mockJobReports);
  const [currentPage, setCurrentPage] = useState(1);
  const reportsPerPage = 2;

  const indexOfLastReport = currentPage * reportsPerPage;
  const indexOfFirstReport = indexOfLastReport - reportsPerPage;
  const currentReports = jobReports.slice(indexOfFirstReport, indexOfLastReport);
  const totalPages = Math.ceil(jobReports.length / reportsPerPage);

  const handleIgnore = (reportId) => {
    setJobReports(jobReports.filter(report => report.id !== reportId));
  };

  const handleDelete = (reportId) => {
    setJobReports(jobReports.filter(report => report.id !== reportId));
    // Add API call to delete the job
  };

  return (
    <div className="admin-dashboard">
      <h1>Reports Management</h1>
      <Navigation activeTab={activeTab} onTabChange={setActiveTab} />
      
      {activeTab === 'jobs' ? (
        <div className="reports-container">
          {currentReports.map(report => (
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