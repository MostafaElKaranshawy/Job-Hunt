import React, { useEffect, useState } from "react";
import "./skills.css";
import { useParams } from "react-router-dom";
import { getAllSkills } from "../../services/userProfileService";
export default function Skills({ skills, changeUserSkills, addSkill, removeSkill }) {
    const {username} = useParams()
    const [allSkills, setAllSkills] = useState([
        { id: 0, skillName: "python" },{ id: 1, skillName: "java" },{ id: 2, skillName: "javascript" },{ id: 3, skillName: "c++" },{ id: 4, skillName: "c#" },{ id: 5, skillName: "ruby" },
        { id: 6, skillName: "go" },
        { id: 7, skillName: "swift" },
        { id: 8, skillName: "kotlin" },
        { id: 9, skillName: "typescript" },
        { id: 10, skillName: "php" },
        { id: 11, skillName: "r" },
        { id: 12, skillName: "scala" },
        { id: 13, skillName: "rust" },
        { id: 14, skillName: "perl" },
        { id: 15, skillName: "haskell" },
        { id: 16, skillName: "dart" },
        { id: 17, skillName: "elixir" },
        { id: 18, skillName: "objective-c" },
        { id: 19, skillName: "matlab" },
        { id: 20, skillName: "shell" },
        { id: 21, skillName: "lua" },
        { id: 22, skillName: "visual basic" },
        { id: 23, skillName: "groovy" },
        { id: 24, skillName: "fortran" },
        { id: 25, skillName: "ada" },
        { id: 26, skillName: "cobol" },
        { id: 27, skillName: "sql" },
        { id: 28, skillName: "assembly" },
        { id: 29, skillName: "delphi" },
        { id: 30, skillName: "julia" },
        { id: 31, skillName: "bash" },
        { id: 32, skillName: "powershell" },
        { id: 33, skillName: "clojure" },
        { id: 34, skillName: "erlang" },
        { id: 35, skillName: "f#" },
        { id: 36, skillName: "prolog" },
        { id: 37, skillName: "pascal" },
        { id: 38, skillName: "smalltalk" },
        { id: 39, skillName: "vhdl" },
        { id: 40, skillName: "verilog" }
    ]);
    
    const [filteredSkills, setFilteredSkills] = useState([]);
    const [search, setSearch] = useState("");
    useEffect(()=> {
        if(username){
            const skills = getSkills();
            console.log(skills)
            setAllSkills(skills)
        }
    }, username)
    const getSkills = async () => {
        await getAllSkills()
    }
    useEffect(() => {
        if (search.trim() === "") {
            setFilteredSkills([]);
        } else {
            const lowercaseSearch = search.toLowerCase();
            setFilteredSkills(
                allSkills.filter(
                    (skill) =>
                        skill.skillName.toLowerCase().includes(lowercaseSearch) &&
                        !skills.includes(skill)
                )
            );
        }
    }, [search, skills, allSkills]);
    const handleSkillToggle = async (skill) => {
        if (skills.includes(skill)) {
            // Remove from user skills
            removeSkill(skill.id)
            changeUserSkills(skills.filter((userSkill) => userSkill.id !== skill.id));
        } else {
            // Add to user skills
            addSkill(skill.id)
            changeUserSkills([...skills, skill]);
        }
    };

    return (
        <div className="skills">
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
            </div>
        </div>
    );
}
