import React, { useState, useEffect } from "react";
import "./skillSection.css";
import axios from "axios";

export default function SkillSection({ onChange }) {
    const [searchKey, setSearchKey] = useState("");
    const [suggestions, setSuggestions] = useState([]);
    const [skills, setSkills] = useState([]);
    const [suggest, setSuggest] = useState(false);
    const threshold = 1; 

    const handleInputChange = (e) => {
        const input = e.target.value;
        setSearchKey(input);
        if (input.length >= threshold) setSuggest(true);
        else setSuggest(false);
    };

    const handleSuggestionClick = (skill) => {
        if (!skills.includes(skill)) {
            setSkills([...skills, skill]);
        }
        setSearchKey("");
        setSuggestions([]);
        setSuggest(false);
    };

    const handleRemoveSkill = (skillToRemove) => {
        setSkills(skills.filter((skill) => skill !== skillToRemove));
    };

    const fetchSkills = async (query) => {
        try {
            const response = await axios.get(`https://api.apilayer.com/skills?q=${query}`, {
                headers: { apikey: "AfAbEfRxDYiRaNIzQHh54FUF2wSn2unP" }
            });
            return response.data;
        } catch (error) {
            console.error("Error fetching skills:", error);
            return [];
        }
    };

    useEffect(() => {
        if (searchKey.length < threshold || !suggest) return;
        fetchSkills(searchKey)
            .then((skillsData) => {
                setSuggestions(skillsData);
            })
            .catch(() => {
                setSuggestions([]);
            });
    }, [searchKey]);

    useEffect(() => {
        onChange(skills);
    }, [skills]);

    return (
        <div className="section-container">
            <h2 className="section-header">Skills</h2>
            <div className="section">
                <label>Add Skills</label>
                <br />
                <input
                    id="skills-input"
                    type="text"
                    value={searchKey}
                    placeholder="Search skills here"
                    onChange={handleInputChange}
                />
                {suggest && (
                    <div className="suggestions">
                        {suggestions.map((skill, index) => (
                            <div
                                key={index}
                                className="suggest-item"
                                onClick={() => handleSuggestionClick(skill)}
                            >
                                {skill}
                            </div>
                        ))}
                    </div>
                )}
                <br /> <br />

                <div className="skills-list">
                    {skills.map((skill, index) => (
                        <div key={index} className="skill-item">
                            {skill}
                            <button
                                className="remove-skill"
                                onClick={() => handleRemoveSkill(skill)}
                            >
                                x
                            </button>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}