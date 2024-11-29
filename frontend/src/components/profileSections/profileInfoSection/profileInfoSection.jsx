import React, {useEffect, useState} from "react";
import { useParams } from "react-router-dom";

import Section from "../../section/section";

import "./profileInfoSection.css";
import profileHolder from "../../../assets/profile.png";


export default function ProfileInfoSection() {
    const {userName} = useParams();
    const [user, setUser] = useState({
        name: "",
        email: "",

    });
    const [userSections, setUserSections] = useState([
        {
            sectionName: "Personal Info",
            sectionFields:[
                {
                    fieldName: "Name",
                    fieldValue: "Mustafa Elkaranshawy"
                },
                {
                    fieldName: "Email",
                    fieldValue: "mostafaelkaranshawy0@gmail.com"
                },
            ]
        },
        {
            sectionName: "Education",
            sectionFields:[
                {
                    fieldName: "School",
                    fieldValue: "Cairo University"
                },
                {
                    fieldName: "Major",
                    fieldValue: "Computer Science"
                },
                {
                    fieldName: "Graduation Year",
                    fieldValue: "2020"
                }
            ]
        },
        {
            sectionName: "Experience",
            sectionFields:[
                {
                    fieldName: "Company",
                    fieldValue: "Google"
                },
                {
                    fieldName: "Position",
                    fieldValue: "Software Engineer"
                },
                {
                    fieldName: "Start Date",
                    fieldValue: "2020"
                },
                {
                    fieldName: "End Date",
                    fieldValue: "2021"
                }
            ]
        }
    ]);

    useEffect(() => {
        setUser({name: "Mustafa Elkaranshawy", email: "mostafaelkaranshawy@gmail.com"})
    }, [])

    return (
        <div className="profile-info-section">
            <div className="profile-picture">
                <img src={profileHolder}/>
            </div>
            <div className="profile-data-sections">
                {userSections && 
                    userSections.map((section, index) => {
                        return (
                            <Section sectionData={section} key={index}/>
                        )
                    })
                }
            </div>
        </div>
    )
}