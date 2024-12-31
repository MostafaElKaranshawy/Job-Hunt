import React from 'react';
import './styles/userDetails.css';

const UserDetails = ({ user }) => {
  return (
    <div className="user-details">
      <h2>{user.username}</h2>
      <div className="user-info">
        <p><strong>User ID:</strong> {user.userId}</p>
        <p><strong>Email:</strong> {user.email}</p>
        <p><strong>Join Date:</strong> {new Date(user.joinDate).toLocaleDateString()}</p>
        <p><strong>Reports History:</strong></p>
        <ul className="reports-history">
          {user.reportsHistory?.map((report, index) => (
            <li key={index}>
              <span className="report-date">{new Date(report.date).toLocaleDateString()}</span>
              <span className="report-reason">{report.reason}</span>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default UserDetails;