import React from 'react';
import './styles/navigation.css';

const Navigation = ({ activeTab, onTabChange }) => {
  return (
    <nav className="navigation">
      <button 
        className={`nav-button ${activeTab === 'jobs' ? 'active' : ''}`}
        onClick={() => onTabChange('jobs')}
      >
        Reported Jobs
      </button>
      <button 
        className={`nav-button ${activeTab === 'users' ? 'active' : ''}`}
        onClick={() => onTabChange('users')}
      >
        Reported Users
      </button>
    </nav>
  );
};

export default Navigation;