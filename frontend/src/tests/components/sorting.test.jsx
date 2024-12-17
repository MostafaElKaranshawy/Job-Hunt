import { describe, expect, test, vi, beforeEach } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import Sorting from "../../components/sorting/Sorting";
import { sortOptions } from "../../constants/sortOptions.js";

describe("Sorting Component", () => {
    const mockOnSortChange = vi.fn(); // Mock function for onSortChange

    beforeEach(() => {
        mockOnSortChange.mockClear(); 
    });

    test("renders the dropdown with correct options", () => {
        render(<Sorting sortBy="DateDesc" onSortChange={mockOnSortChange} />);

        expect(screen.getByLabelText(/Sort By:/i)).toBeInTheDocument();


        const options = screen.getAllByRole("option");
        expect(options).toHaveLength(4);
        expect(options[0].textContent).toBe(sortOptions[0].label);
        expect(options[1].textContent).toBe(sortOptions[1].label);
        expect(options[2].textContent).toBe(sortOptions[2].label);
        expect(options[3].textContent).toBe(sortOptions[3].label);
    });

    test("calls onSortChange for each sorting option", () => {
        render(<Sorting sortBy="DateDesc" onSortChange={mockOnSortChange} />);

        const select = screen.getByLabelText(/Sort By:/i);

        sortOptions.forEach((option) => {
            // Simulate selecting the current option
            fireEvent.change(select, { target: { value: option.value } });

            expect(mockOnSortChange).toHaveBeenCalledWith(option.value);
        });

        expect(mockOnSortChange).toHaveBeenCalledTimes(sortOptions.length);
    });

    test("shows the correct selected option based on sortBy prop", () => {
        render(<Sorting sortBy="SalaryDesc" onSortChange={mockOnSortChange} />);

        const select = screen.getByLabelText(/Sort By:/i);

        // Verify the correct option is selected
        expect(select.value).toBe("SalaryDesc");
    });
});
