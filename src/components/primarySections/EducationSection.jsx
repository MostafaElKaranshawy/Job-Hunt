import React, { useState, useEffect } from "react";
import SimpleText from "../InputTypes/SimpleText";
import DateInput from "../InputTypes/DateInput";
import DropDown from "../InputTypes/DropDown";
import axios from "axios";

export default function EducationSection({ onChange }) {
    const commonDegrees = [
        "Associate of Arts (AA)",
        "Associate's Degree",
        "Bachelor of Arts (BA)",
        "Bachelor of Commerce (BCom)",
        "Bachelor of Engineering (BE)",
        "Bachelor of Fine Arts (BFA)",
        "Bachelor of Science (BS)",
        "Doctor of Education (EdD)",
        "Doctor of Medicine (MD)",
        "Doctor of Philosophy (PhD)",
        "Diploma",
        "Diploma in Engineering",
        "Engineering Degree",
        "Law Degree",
        "Master of Arts (MA)",
        "Master of Business Administration (MBA)",
        "Master of Public Administration (MPA)",
        "Master of Science (MS)",
        "Certification",
        "High School Diploma",
        "Other"
    ];

    const [highestDegree, setHighestDegree] = useState("");
    const [fieldOfStudy, setFieldOfStudy] = useState("");
    const [searchKey, setSearchKey] = useState("");
    const [suggestions, setSuggestions] = useState([]);
    const [suggest, setSuggest] = useState(true);
    const [graduationYear, setGraduationYear] = useState("");
    const threshold = 2;

    const handleSelect = (e) => {
        const x = e.target.value;
        setSearchKey(x);

        if (x.length >= threshold) setSuggest(true);
        else setSuggest(false);
    };

    const handleSuggestionClick = (university) => {
        setSearchKey(university);
        setSuggestions([]);
        setSuggest(false);
    };

    async function searchResult(key) {
        try {
            const response = await axios.get('http://universities.hipolabs.com/search?name=' + key);
            return response.data.filter(university => university.name.toLowerCase().startsWith(key.toLowerCase()));
        } catch (error) {
            console.error('Error fetching universities:', error);
            throw error;
        }
    }

    useEffect(() => {
        if (searchKey.length < threshold || !suggest) {
            setSuggest(false);
            return;
        }

        searchResult(searchKey)
            .then(filteredUniversities => {
                const uni = filteredUniversities.map(uni => uni.name);
                setSuggestions(uni);
            })
            .catch(() => {
                setSuggestions([]);
            });
    }, [searchKey]);

    useEffect(() => {
        onChange({
            highestDegree,
            fieldOfStudy,
            university: searchKey,
            graduationYear
        });
    }, [highestDegree, fieldOfStudy, searchKey, graduationYear]);

    return (
        <div className="section-container">
            <h2 className="section-header">Education</h2>
            <div className="section">
                <DropDown name="Degree" options={commonDegrees} value={highestDegree} onChange={setHighestDegree} />
                <br />

                <SimpleText name="Field Of Study" value={fieldOfStudy} onChange={setFieldOfStudy} />
                <br />

                <label>University/Institution Name</label>
                <br />
                <input type="text" value={searchKey} placeholder="Search here University Name" onChange={handleSelect} />
                {suggest && <div className="suggestions">
                    {suggestions.map((option, index) => (
                        <div className="suggest-item" key={index} onClick={() => handleSuggestionClick(option)}>
                            {option}
                        </div>
                    ))}
                </div>}
                <br /> <br />
                
                <DateInput name="Start Year" fullDate={false} value={graduationYear} onChange={setGraduationYear} />
                <br />
                <DateInput name="End Year" fullDate={false} value={graduationYear} onChange={setGraduationYear} />
            </div>
        </div>
    );
}