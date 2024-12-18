import React from "react";
import "./Sorting.css";

import { sortOptions } from "../../constants/sortOptions.js";

function Sorting({ sortBy, onSortChange }) {
    return (
        <div className="sort-dropdown">
            <label htmlFor="sort">Sort By: </label>
            <select id="sort" value={sortBy} onChange={(e) => onSortChange(e.target.value)}>
                {sortOptions.map((option) => (
                    <option key={option.value} value={option.value}>
                        {option.label}
                    </option>
                ))}
            </select>
        </div>
    );
}

export default Sorting;
