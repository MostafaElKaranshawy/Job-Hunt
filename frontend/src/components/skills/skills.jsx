import React, { useEffect, useState } from "react";
import "./skills.css";
import { useParams } from "react-router-dom";
import { fetchSkills, getUserSkills, editUserSkills } from "../../services/userProfileService";

export default function Skills({username}) {
    const [filteredSkills, setFilteredSkills] = useState([]);
    const [skills, setSkills] = useState([]);
    const [search, setSearch] = useState("");
    const [isEditable, setIsEditable] = useState(false); // State to manage edit mode

    useEffect(() => {
        if(username){
            getUserSkills(username).then((skills) => {
                setSkills(skills);
            });
        }
    }, [username]);

    useEffect(() => {
        if (search.trim() === "") {
            setFilteredSkills([]);
        } else {
            handleSearch();
        }
    }, [search]);
    const handleSearch = async () => {
        const searchedSkills = await fetchSkills(search.toLowerCase())
        console.log(searchedSkills)
        setFilteredSkills(searchedSkills);
    }
    // Handle toggling skills in the list
    const handleSkillToggle = (skill) => {
        if (skills.some((s) => s === skill)) {
            setSkills(skills.filter((s) => s !== skill));
        } else {
            setSkills([...skills, skill]);
        }
    };
    const addSkill = (skill) =>{
        if (skills.some((s) => s === skill)) {
        } else {
            setSkills([...skills, skill]);
            setFilteredSkills(filteredSkills.filter((s)=> s != skill))
        }
    }
    const removeSkill = (skill)=>{
        if (skills.some((s) => s === skill)) {
            setSkills(skills.filter((s) => s !== skill));
        }
    }

    // Handle the edit button click to toggle edit mode
    const handleEditClick = () => {
        setIsEditable(!isEditable);
    };
    const handleSaveSkills = ()=>{
        setIsEditable(false)

        editUserSkills(username, skills)
    }
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
                        onClick={() => removeSkill(skill)}
                    >
                        {skill}
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
                                onClick={() => addSkill(skill)}
                            >
                                {skill}
                            </div>
                        ))}
                    </div>
                    <div className="section-options">
                        <div className="cancel-button" onClick={() => setIsEditable(false)}>
                            Cancel
                        </div>
                        <div className="save-button" onClick={handleSaveSkills}>
                            Save
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
