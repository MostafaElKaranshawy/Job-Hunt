import { describe, test, expect, vi, beforeEach } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import JobList from "../../components/jobList/JobList";
import { truncateDescription, calculateRelativeTime } from "../../utils/userHomeUtils";
import { locationOptions, employmentTypes, jobLevels, minimumSalary } from "../../constants/filterOptions";

describe("JobList Component", () => {
    const mockHandleExpandJob = vi.fn();
    const mockJobs = [
        {
            id: 1,
            company: { name: "Company A" },
            title: "Frontend Developer",
            workLocation: "REMOTE",
            employmentType: "FULL_TIME",
            level: "MID_LEVEL",
            salary: "80,000",
            postedAt: "2 days ago",
            description: "This is a job description for Frontend Developer.",
        },
        {
            id: 2,
            company: { name: "Company B" },
            title: "Backend Developer",
            workLocation: "ONSITE",
            employmentType: "PART_TIME",
            level: "SENIOR_LEVEL",
            salary: "120,000",
            postedAt: "5 days ago",
            description: "Backend Developer required with experience in Node.js.",
        },
    ];

    beforeEach(() => {
        mockHandleExpandJob.mockClear(); // Reset the mock function before each test
    });

    test("renders a list of job cards", () => {
        render(<JobList jobs={mockJobs} handleExpandJob={mockHandleExpandJob} />);

        // Verify all job titles are rendered
        expect(screen.getByText("Frontend Developer")).toBeInTheDocument();
        expect(screen.getByText("Backend Developer")).toBeInTheDocument();

        // Verify company names are displayed
        expect(screen.getByText("Company A")).toBeInTheDocument();
        expect(screen.getByText("Company B")).toBeInTheDocument();
    });

    test("renders truncated descriptions if longer than the limit", () => {
        render(<JobList jobs={mockJobs} handleExpandJob={mockHandleExpandJob} />);

        // Check that descriptions are truncated correctly
        expect(screen.getByText(truncateDescription(mockJobs[0].description))).toBeInTheDocument();
        expect(screen.getByText(truncateDescription(mockJobs[1].description))).toBeInTheDocument();
    });

    test("calls handleExpandJob when the 'Learn More' button is clicked", () => {
        render(<JobList jobs={mockJobs} handleExpandJob={mockHandleExpandJob} />);

        // Find the first "Learn More" button and click it
        const learnMoreButtons = screen.getAllByText("Learn More");
        fireEvent.click(learnMoreButtons[0]);

        // Verify the mock function was called with the correct job
        expect(mockHandleExpandJob).toHaveBeenCalledTimes(1);
        expect(mockHandleExpandJob).toHaveBeenCalledWith(mockJobs[0]);
    });

    test("renders all job details correctly", () => {
        render(<JobList jobs={mockJobs} handleExpandJob={mockHandleExpandJob} />);

        // Verify job details are displayed correctly
        expect(screen.getByText(mockJobs[0].company.name)).toBeInTheDocument();
        expect(screen.getByText(locationOptions.find((option) => option.id === mockJobs[0].workLocation).label)).toBeInTheDocument();
        expect(screen.getByText(employmentTypes.find((option) => option.id === mockJobs[0].employmentType).label)).toBeInTheDocument();
        expect(screen.getByText(jobLevels.find((option) => option.id === mockJobs[0].level).label)).toBeInTheDocument();
        expect(screen.getByText(`$${mockJobs[0].salary}`)).toBeInTheDocument();
        // expect(screen.getByText("🕛 2 days ago ")).toBeInTheDocument();
    

        expect(screen.getByText(mockJobs[1].company.name)).toBeInTheDocument();
        expect(screen.getByText(locationOptions.find((option) => option.id === mockJobs[1].workLocation).label)).toBeInTheDocument();
        expect(screen.getByText(employmentTypes.find((option) => option.id === mockJobs[1].employmentType).label)).toBeInTheDocument();
        expect(screen.getByText(jobLevels.find((option) => option.id === mockJobs[1].level).label)).toBeInTheDocument();
        expect(screen.getByText(`$${mockJobs[1].salary}`)).toBeInTheDocument();
        // expect(screen.getByText("🕛 5 days ago")).toBeInTheDocument();
    });
});
