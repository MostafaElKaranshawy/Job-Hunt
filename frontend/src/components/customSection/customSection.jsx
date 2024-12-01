import React from "react";

import Section from "../section/section";
import './customSection.css';
export default function CustomSection({ sectionData, sectionChange }) {
    return (
        <div className="custom-section">
            <div className="section-header">
                <div className="section-name">
                    {sectionData.sectionName}
                </div>
                <div className="add-section">
                    +
                </div>
            </div>
            <div className="section-body">
                {sectionData.sectionSections.map((sectionSection, index) => {
                    return (
                        <Section
                            key={index}
                            className="section-field"
                            sectionData={sectionSection}
                            sectionChange={sectionChange}
                        />
                    );
                })}
            </div>
        </div>
    );
}