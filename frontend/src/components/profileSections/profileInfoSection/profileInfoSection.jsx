import React, {useEffect, useState} from "react";
import { useParams } from "react-router-dom";
import "./profileInfoSection.css";
import profileHolder from "../../../assets/profile.png";
export default function ProfileInfoSection() {
    const {userName} = useParams();
    const [user, setUser] = useState({
        name: "",
        email: "",

    });
    useEffect(() => {
        setUser({name: "Mustafa Elkaranshawy", email: "mostafaelkaranshawy@gmail.com"})
    }, [])
    return (
        <div className="profile-info-section">
            <div className="profile-picture">
                <img src={profileHolder}/>
            </div>
            <div className="profile-info">                    
                {user && 
                    Object.entries(user).map(([key, value]) => {
                        return (
                            <div className="profile-info-attribute" key={key}>
                                <div className="profile-info-label">
                                    {key.charAt(0).toUpperCase() + key.slice(1)}
                                </div>
                                <input className="profile-info-value" value={value}/>
                            </div>
                        );
                    })
                }

            </div>
        </div>
    )
}