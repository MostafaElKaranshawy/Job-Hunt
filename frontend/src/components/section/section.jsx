import React, { useState } from "react";

import "./section.css";

export default function Section({sectionData}) {
    const [section, setSection] = useState(sectionData);

    const handleSectionChange = (e) => {
        setSection({...section, [e.target.name]: e.target.value});
    }
    return (
        <div className="section">
            <div className="section-name">
                {section.sectionName}
            </div>
            <div className="section-fields">
                {section.sectionFields.map((field, index) => {
                    return (
                        <div className="section-field" key={index}>
                            <div className="field-name">
                                {field.fieldName}
                            </div>
                            <input className="field-value" value={field.fieldValue} name={field.fieldName} onChange={handleSectionChange}/>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}