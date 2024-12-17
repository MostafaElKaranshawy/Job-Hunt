import { describe, expect, test} from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import SearchBar from '../../components/searchBar/SearchBar';

describe('SearchBar Component', () => {

    // Test if the SearchBar component (both input and button) renders correctly
    test('renders the input and search button', () => {
        render(<SearchBar onSearch={() => {}} />);

        const inputElement = screen.getByTestId('search-input');
        expect(inputElement).toBeInTheDocument();

        const buttonElement = screen.getByRole('button', { name: /search job/i });
        expect(buttonElement).toBeInTheDocument();
    });

    test('updates the input value as the user types', () => {
        render(<SearchBar onSearch={() => {}} />);
        
        const inputElement = screen.getByTestId('search-input');

        // Simulate typing in the input field
        fireEvent.change(inputElement, { target: { value: 'Software Engineer' } });

        // Check if the input value is updated
        expect(inputElement.value).toBe('Software Engineer');
    });


    test('calls onSearch callback with correct query when form is submitted', () => {
        const mockOnSearch = vi.fn(); // Mock function for onSearch
        render(<SearchBar onSearch={mockOnSearch} />);

        const inputElement = screen.getByTestId('search-input');
        const buttonElement = screen.getByRole('button', { name: /search job/i });

        // Simulate typing and submitting the form
        fireEvent.change(inputElement, { target: { value: 'Frontend Developer' } });
        fireEvent.click(buttonElement);

        // Check if onSearch was called with the correct argument
        expect(mockOnSearch).toHaveBeenCalledWith('Frontend Developer');
    });

    // test('prevents default form submission', () => {
    //     const mockOnSearch = vi.fn();
    //     render(<SearchBar onSearch={mockOnSearch} />);

    //     const formElement = screen.getByTestId("search-form");
    //     const preventDefaultMock = vi.fn();

    //     // Simulate form submission
    //     fireEvent.submit(formElement, { preventDefault: preventDefaultMock });

    //     // Check if preventDefault was called
    //     expect(preventDefaultMock).toHaveBeenCalled();
    // });


});
