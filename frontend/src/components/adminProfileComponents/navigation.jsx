import React, { useState } from 'react';
import './styles/navigation.css';
import ConfirmationBox from '../confirmationBox/confirmationBox';

const Navigation = ({ activeTab, onTabChange }) => {

    const [showLogOutConfirmation, setShowLogOutConfirmation] = useState(false);
    
    
    return (
        <nav className="navigation">
            {
                showLogOutConfirmation && (
                    <ConfirmationBox
                        content="Are you sure you want to log out?"
                        cancel={() => setShowLogOutConfirmation(false)}
                        confirm={() => {
                            document.cookie = ";";
                            window.location.href = '/';
                        }}
                    />
                )
            }
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
            <div className="log-out" onClick={() => {
                setShowLogOutConfirmation(true);
            }}>
                <i className="fa-solid fa-right-from-bracket"></i>
            </div>
        </nav>
    );
};

export default Navigation;