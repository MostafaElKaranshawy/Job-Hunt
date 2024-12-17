import React, { useState } from 'react';
import './searchBar.css';

function SearchBar( {onSearch} ) {
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearchLocally = (e) => {
        e.preventDefault();
       
        onSearch(searchQuery.trim());
    };

    return (
        <form 
            className="search-container" 
            data-testid="search-form"
            onSubmit={handleSearchLocally} 
        >
            <div className="search-input-container">
                <input
                    type="text"
                    placeholder="Search by company name or job title (e.g., 'Google', 'Software Engineer')"
                    data-testid="search-input"
                    className="search-input"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
                <button 
                    type="submit" 
                    className="search-button"
                    disabled={searchQuery.trim() == ''}
                >
                    Search job
                </button>
            </div>
        </form>
    );
}

export default SearchBar;