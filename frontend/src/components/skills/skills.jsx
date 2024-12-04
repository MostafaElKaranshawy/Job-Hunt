import React, { useEffect, useState } from "react";
import "./skills.css";
import { fetchSkills, getUserSkills, editUserSkills } from "../../services/userProfileService";

export default function Skills({username}) {
    const [filteredSkills, setFilteredSkills] = useState([]);
    const [skills, setSkills] = useState([]);
    const [search, setSearch] = useState("");
    const [isEditable, setIsEditable] = useState(false); // State to manage edit mode
    const [skillsLimit, setSkillsLimit] = useState(false)
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
        if(search)
            setFilteredSkills(searchedSkills.filter(skill => !skills.includes(skill)));
    }
    // Handle toggling skills in the list
    const addSkill = (skill) =>{
        if(!isEditable)return
        if (skills.some((s) => s === skill)) {
        } else {
            if(skills.length >= 20){
                setSkillsLimit(true)
                return
            }
            setSkills([...skills, skill]);
            setFilteredSkills(filteredSkills.filter((s)=> s != skill))
        }
    }
    const removeSkill = (skill)=>{
        if(!isEditable)return
        if (skills.some((s) => s === skill)) {
            setSkills(skills.filter((s) => s !== skill));
        }
        if(skills.length <= 10)
            setSkillsLimit(false)
    }

    // Handle the edit button click to toggle edit mode
    const handleEditClick = () => {
        setIsEditable(!isEditable);
    };
    const handleSaveSkills = async ()=>{
        handleCancel();
        await editUserSkills(username, skills)
    }
    const handleCancel = async ()=>{
        setIsEditable(false)
        setSkillsLimit(false)
        setFilteredSkills([])
        setSearch("")
        const skillsData = await getUserSkills(username)
        setSkills(skillsData)
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
                        className={`skills-search ${skillsLimit?"error" :""}`}
                        placeholder="Search for skills..."
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                    {
                        skillsLimit &&
                        <div className="search-limit-error error">
                            You can only add up to 20 skills
                        </div>
                    }
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
                        <div className="cancel-button" onClick={handleCancel}>
                            Cancel
                        </div>
                        <div className="save-button" onClick={handleSaveSkills} >
                            Save
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
