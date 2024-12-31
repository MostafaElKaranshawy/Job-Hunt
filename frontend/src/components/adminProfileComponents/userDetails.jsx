import React from 'react';
import './styles/userDetails.css';

const UserDetails = ({ user }) => {
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
        <div className="user-details">
            <h2>{user.firstName} {user.lastName}</h2>
            <div className="user-info">
                <p><strong>User ID:</strong> {user.id}</p>
                <p><strong>Username:</strong> {user.username || 'N/A'}</p>
                <p><strong>Email:</strong> {user.email || 'N/A'}</p>
                <p><strong>Address:</strong> {user.address || 'N/A'}</p>
                <p><strong>City:</strong> {user.city || 'N/A'}</p>
                <p><strong>State:</strong> {user.state || 'N/A'}</p>
                <p><strong>Country:</strong> {user.country || 'N/A'}</p>
                <p><strong>Phone Number:</strong> {user.phoneNumber || 'N/A'}</p>
                <p><strong>Join Date:</strong> {formattedDate(user.joinedAt)}</p>
            </div>
        </div>
    );
};

export default UserDetails;
