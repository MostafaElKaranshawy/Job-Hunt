import React, { useState } from 'react';
import UserReportCard from './userReportCard';
import Pagination from './pagination';

// Mock data for user reports
const mockUserReports = [
  {
    id: 1,
    userId: "USER-001",
    username: "JohnDoe",
    email: "john@example.com",
    joinDate: "2023-01-15",
    reportedBy: "Alice Smith",
    reason: "Inappropriate behavior",
    reportDate: "2024-03-10",
    reportsHistory: [
      { date: "2024-03-10", reason: "Inappropriate behavior" },
      { date: "2024-02-15", reason: "Spam messages" }
    ]
  },
  {
    id: 2,
    userId: "USER-001",
    username: "JohnDoe",
    email: "john@example.com",
    joinDate: "2023-01-15",
    reportedBy: "Alice Smith",
    reason: "Inappropriate behavior",
    reportDate: "2024-03-10",
    reportsHistory: [
      { date: "2024-03-10", reason: "Inappropriate behavior" },
      { date: "2024-02-15", reason: "Spam messages" }
    ]
  },
  {
    id: 3,
    userId: "USER-001",
    username: "JohnDoe",
    email: "john@example.com",
    joinDate: "2023-01-15",
    reportedBy: "Alice Smith",
    reason: "Inappropriate behavior",
    reportDate: "2024-03-10",
    reportsHistory: [
      { date: "2024-03-10", reason: "Inappropriate behavior" },
      { date: "2024-02-15", reason: "Spam messages" }
    ]
  },
  {
    id: 4,
    userId: "USER-001",
    username: "JohnDoe",
    email: "john@example.com",
    joinDate: "2023-01-15",
    reportedBy: "Alice Smith",
    reason: "Inappropriate behavior",
    reportDate: "2024-03-10",
    reportsHistory: [
      { date: "2024-03-10", reason: "Inappropriate behavior" },
      { date: "2024-02-15", reason: "Spam messages" }
    ]
  },
  {
    id: 5,
    userId: "USER-001",
    username: "JohnDoe",
    email: "john@example.com",
    joinDate: "2023-01-15",
    reportedBy: "Alice Smith",
    reason: "Inappropriate behavior",
    reportDate: "2024-03-10",
    reportsHistory: [
      { date: "2024-03-10", reason: "Inappropriate behavior" },
      { date: "2024-02-15", reason: "Spam messages" }
    ]
  },
  {
    id: 6,
    userId: "USER-001",
    username: "JohnDoe",
    email: "john@example.com",
    joinDate: "2023-01-15",
    reportedBy: "Alice Smith",
    reason: "Inappropriate behavior",
    reportDate: "2024-03-10",
    reportsHistory: [
      { date: "2024-03-10", reason: "Inappropriate behavior" },
      { date: "2024-02-15", reason: "Spam messages" }
    ]
  },
  {
    id: 7,
    userId: "USER-001",
    username: "JohnDoe",
    email: "john@example.com",
    joinDate: "2023-01-15",
    reportedBy: "Alice Smith",
    reason: "Inappropriate behavior",
    reportDate: "2024-03-10",
    reportsHistory: [
      { date: "2024-03-10", reason: "Inappropriate behavior" },
      { date: "2024-02-15", reason: "Spam messages" }
    ]
  },
];

const UserReports = () => {
  const [reports, setReports] = useState(mockUserReports);
  const [currentPage, setCurrentPage] = useState(1);
  const reportsPerPage = 2;

  const indexOfLastReport = currentPage * reportsPerPage;
  const indexOfFirstReport = indexOfLastReport - reportsPerPage;
  const currentReports = reports.slice(indexOfFirstReport, indexOfLastReport);
  const totalPages = Math.ceil(reports.length / reportsPerPage);

  const handleIgnore = (reportId) => {
    setReports(reports.filter(report => report.id !== reportId));
  };

  const handleBan = (reportId) => {
    setReports(reports.filter(report => report.id !== reportId));
    // Add API call to ban user
  };

  return (
    <div className="reports-container">
      {currentReports.map(report => (
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