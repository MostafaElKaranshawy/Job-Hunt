import React, { useState } from 'react';
import './searchBar.css';

function SearchBar( {onSearch} ) {
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearchLocally = (e) => {
        e.preventDefault();
       
        onSearch(searchQuery);

        console.log('Searching for:', searchQuery);
    };

    return (
        <form className="search-container" onSubmit={handleSearchLocally}>
            <div className="search-input-container">
                <input
                    type="text"
                    placeholder="What position are you looking for?"
                    className="search-input"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                />
                <button type="submit" className="search-button">
                    Search job
                </button>
            </div>
        </form>
    );
}

export default SearchBar;