import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { vi } from 'vitest';
import UserHome from '../../pages/userHome/userHome.jsx';
import { fetchJobs } from '../../services/homeService';
import { locationOptions, employmentTypes, jobLevels } from '../../constants/filterOptions';

// Mock the child components
vi.mock('../../components/header/header', () => ({
    default: () => <div data-testid="header">Header</div>,
}));

vi.mock('../../components/searchBar/SearchBar', () => ({
    default: ({ onSearch }) => (
        <input
            data-testid="search-bar"
            placeholder="Search"
            onChange={(e) => onSearch(e.target.value)}
        />
    ),
}));

vi.mock('../../components/filters/Filters', () => ({
    default: ({ filters, onFilterChange }) => (
        <div data-testid="filters">
            Filters
            <button onClick={() => onFilterChange('workLocation', 'ONSITE')}>
                Apply Onsite Filter
            </button>
        </div>
    ),
}));

vi.mock('../../components/sorting/Sorting', () => ({
    default: ({ sortBy, onSortChange }) => (
        <select
            data-testid="sorting"
            value={sortBy}
            onChange={(e) => onSortChange(e.target.value)}
        >
            <option value="DateDesc">Date Desc</option>
            <option value="SalaryAsc">Salary Asc</option>
        </select>
    ),
}));

vi.mock('../../components/jobList/JobList', () => ({
    default: ({ jobs, handleExpandJob }) => (
        <div data-testid="job-list">
            {jobs.map((job, index) => (
                <button
                    key={index}
                    onClick={() => handleExpandJob(job)}
                    data-testid="job-item"
                >
                    {job.title}
                </button>
            ))}
        </div>
    ),
}));

vi.mock('../../services/homeService', () => ({
    fetchJobs: vi.fn(),
}));

describe('UserHome', () => {
    beforeEach(() => {
        vi.resetAllMocks();
    });

    test('renders the main components correctly', () => {
        render(<UserHome />);

        expect(screen.getByTestId('header')).toBeInTheDocument();
        expect(screen.getByTestId('search-bar')).toBeInTheDocument();
        expect(screen.getByTestId('filters')).toBeInTheDocument();
        expect(screen.getByTestId('sorting')).toBeInTheDocument();
        expect(screen.getByText(/Find your right jobs offers today/i)).toBeInTheDocument();
    });

    test('fetches and displays jobs correctly', async () => {
        const mockJobs = [
            { id: 1, title: 'Frontend Developer', workLocation: 'ONSITE' },
            { id: 2, title: 'Backend Developer', workLocation: 'REMOTE' },
        ];
        fetchJobs.mockResolvedValue({ jobs: mockJobs, totalJobs: 2 });

        render(<UserHome />);

        await waitFor(() => {
            expect(fetchJobs).toHaveBeenCalledWith(
                {
                    workLocation: '',
                    employmentType: '',
                    jobLevel: '',
                    salary: '0',
                    searchQuery: '',
                    sortBy: 'DateDesc',
                },
                0,
                5
            );
        });

        expect(screen.getAllByTestId('job-item')).toHaveLength(2);
        expect(screen.getByText('Frontend Developer')).toBeInTheDocument();
        expect(screen.getByText('Backend Developer')).toBeInTheDocument();
    });

    test('applies filters correctly', async () => {
        render(<UserHome />);

        const filterButton = screen.getByText('Apply Onsite Filter');
        fireEvent.click(filterButton);

        await waitFor(() => {
            expect(fetchJobs).toHaveBeenCalledWith(
                expect.objectContaining({
                    workLocation: 'ONSITE',
                }),
                0,
                5
            );
        });
    });

    test('handles search input correctly', async () => {
        render(<UserHome />);

        const searchBar = screen.getByTestId('search-bar');
        fireEvent.change(searchBar, { target: { value: 'developer' } });

        await waitFor(() => {
            expect(fetchJobs).toHaveBeenCalledWith(
                expect.objectContaining({
                    searchQuery: 'developer',
                }),
                0,
                5
            );
        });
    });

    test('updates sorting correctly', async () => {
        render(<UserHome />);

        const sortingDropdown = screen.getByTestId('sorting');
        fireEvent.change(sortingDropdown, { target: { value: 'SalaryAsc' } });

        await waitFor(() => {
            expect(fetchJobs).toHaveBeenCalledWith(
                expect.objectContaining({
                    sortBy: 'SalaryAsc',
                }),
                0,
                5
            );
        });
    });

    test('handles pagination correctly', async () => {
        const mockJobs = [
            { id: 1, title: 'Frontend Developer' },
            { id: 2, title: 'Backend Developer' },
        ];
        fetchJobs.mockResolvedValue({ jobs: mockJobs, totalJobs: 10 });

        render(<UserHome />);

        const nextButton = await waitFor(() => screen.getByTestId('next-button'));
        fireEvent.click(nextButton);

        await waitFor(() => {
            expect(fetchJobs).toHaveBeenCalledWith(
                expect.anything(),
                1,
                5
            );
        });

        expect(nextButton).toBeDisabled(); 
    });
});
