import React, { useEffect, useState } from "react";
import "./skills.css";
import { useParams } from "react-router-dom";
import { fetchSkills } from "../../services/userProfileService";

export default function Skills() {
    const { username } = useParams();
    const [allSkills, setAllSkills] = useState([]);
    const [filteredSkills, setFilteredSkills] = useState([]);
    const [skills, setSkills] = useState([]);
    const [search, setSearch] = useState("");
    const [isEditable, setIsEditable] = useState(false); // State to manage edit mode

    // Fetch skills when the username changes
    const fetchAndSetSkills = async () => {
        try {
            const skills = await fetchSkills();
            setAllSkills(skills);
        } catch (error) {
            console.error("Error fetching skills:", error);
        }
    };

    useEffect(() => {
        fetchAndSetSkills();
    }, [username]);

    useEffect(() => {
        if (search.trim() === "") {
            setFilteredSkills([]);
        } else {
            const lowercaseSearch = search.toLowerCase();
            setFilteredSkills(
                allSkills.filter(
                    (skill) =>
                        skill.skillName.toLowerCase().includes(lowercaseSearch) &&
                        !skills.some((s) => s.skillName === skill.skillName)
                )
            );
        }
    }, [search, skills, allSkills]);

    // Handle toggling skills in the list
    const handleSkillToggle = (skill) => {
        if (skills.some((s) => s.skillName === skill.skillName)) {
            setSkills(skills.filter((s) => s.skillName !== skill.skillName));
        } else {
            setSkills([...skills, skill]);
        }
    };

    // Handle the edit button click to toggle edit mode
    const handleEditClick = () => {
        setIsEditable(!isEditable);
    };

    return (
        <div className="skills">
            <div className="section-edit" onClick={handleEditClick}>
                <i className="fas fa-pencil-alt"></i>
            </div>
            <div className="skills-name">Skills</div>
            <div className="skills-list">
                {skills.map((skill, index) => (
                    <div
                        className="skill user-skill"
                        key={index}
                        onClick={() => handleSkillToggle(skill)}
                    >
                        {skill.skillName}
                    </div>
                ))}
            </div>

            {/* Conditionally render the editable section */}
            {isEditable && (
                <div className="skills-fields">
                    <input
                        className="skills-search"
                        placeholder="Search for skills..."
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                    <div className="skills-filter">
                        {filteredSkills.map((skill, index) => (
                            <div
                                className="skill add-skill"
                                key={index}
                                onClick={() => handleSkillToggle(skill)}
                            >
                                {skill.skillName}
                            </div>
                        ))}
                    </div>
                    <div className="section-options">
                        <div className="cancel-button" onClick={() => setIsEditable(false)}>
                            Cancel
                        </div>
                        <div className="save-button" onClick={() => setIsEditable(false)}>
                            Save
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
