import React from 'react';
import './styles/pagination.css';

const Pagination = ({ currentPage, totalPages, onPageChange }) => {
    return (
        <div className="pagination">
            <button
                className="pagination-btn"
                disabled={currentPage === 1 ||totalPages === 0}
                onClick={() => onPageChange(currentPage - 1)}
            >
                Previous
            </button>

            <span className="page-info">
                Page {totalPages == 0? 0 : currentPage} of {totalPages}
            </span>

            <button
                className="pagination-btn"
                disabled={currentPage === totalPages || totalPages === 0}
                onClick={() => onPageChange(currentPage + 1)}
            >
                Next
            </button>
        </div>
    );
}

export default Pagination;