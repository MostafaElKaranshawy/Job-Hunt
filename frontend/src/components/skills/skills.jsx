import React, { useEffect, useState } from "react";
import "./skills.css";

export default function Skills({ skills, changeUserSkills }) {
    const [allSkills] = useState([
        "python", "java", "javascript", "c++", "c#", "ruby", "go", "swift",
        "kotlin", "typescript", "php", "r", "scala", "rust", "perl", "haskell",
        "dart", "elixir", "objective-c", "matlab", "shell", "lua", "visual basic",
        "groovy", "fortran", "ada", "cobol", "sql", "assembly", "delphi",
        "julia", "bash", "powershell", "clojure", "erlang", "f#", "prolog",
        "pascal", "smalltalk", "vhdl", "verilog", "abap", "awk", "sas", "apl",
        "postscript", "ocaml", "tcl", "nim", "crystal", "scheme",
    ]);
    const [filteredSkills, setFilteredSkills] = useState([]);
    const [search, setSearch] = useState("");

    useEffect(() => {
        if (search.trim() === "") {
            setFilteredSkills([]);
        } else {
            const lowercaseSearch = search.toLowerCase();
            setFilteredSkills(
                allSkills.filter(
                    (skill) =>
                        skill.toLowerCase().includes(lowercaseSearch) &&
                        !skills.includes(skill)
                )
            );
        }
    }, [search, skills, allSkills]);

    const handleSkillToggle = (skill) => {
        if (skills.includes(skill)) {
            // Remove from user skills
            changeUserSkills(skills.filter((userSkill) => userSkill !== skill));
        } else {
            // Add to user skills
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
                        {skill}
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
                            {skill}
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}
