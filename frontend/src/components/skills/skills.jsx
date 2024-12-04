import React, { useEffect, useState } from "react";
import "./skills.css";
import { useParams } from "react-router-dom";
import { fetchSkills, getUserSkills, editUserSkills } from "../../services/userProfileService";

export default function Skills({username}) {
    const [allSkills, setAllSkills] = useState([]);
    const [filteredSkills, setFilteredSkills] = useState([]);
    const [skills, setSkills] = useState([]);
    const [search, setSearch] = useState("");
    const [isEditable, setIsEditable] = useState(false); // State to manage edit mode

    // Fetch skills when the username changes
    const fetchAndSetSkills = async () => {
        try {
            // const skills = await fetchSkills();
            const skills = [
                "JavaScript","React","Node.js","Express.js","Spring Boot","HTML","CSS","SQL","NoSQL","MongoDB","MySQL","PostgreSQL","Docker","Kubernetes","AWS","Git","GitHub","Python","Java","C++","C#","TypeScript","Angular","Vue.js","Bootstrap","Tailwind CSS","REST API","GraphQL","Jenkins","CI/CD","Webpack","Babel",
                "Redux","Machine Learning","Data Structures","Algorithms","OOP","Design Patterns","Unit Testing","Integration Testing","Jest","Mocha","Chai","Cypress","Selenium","Figma","Adobe XD","Agile Methodologies","Scrum","Linux","Command Line"
            ];
            
            setAllSkills(skills);
        } catch (error) {
            console.error("Error fetching skills:", error);
        }
    };

    useEffect(() => {
        fetchAndSetSkills();
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
            const lowercaseSearch = search.toLowerCase();
            setFilteredSkills(
                allSkills.filter(
                    (skill) =>
                        skill.toLowerCase().includes(lowercaseSearch) &&
                        !skills.some((s) => s === skill)
                )
            );
        }
    }, [search, skills, allSkills]);

    // Handle toggling skills in the list
    const handleSkillToggle = (skill) => {
        if (skills.some((s) => s === skill)) {
            setSkills(skills.filter((s) => s !== skill));
        } else {
            setSkills([...skills, skill]);
        }
    };

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
                        onClick={() => handleSkillToggle(skill)}
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
                                onClick={() => handleSkillToggle(skill)}
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
