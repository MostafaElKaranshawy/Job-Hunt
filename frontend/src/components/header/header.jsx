import React from "react";
import { NavLink } from "react-router-dom";
import "./header.css";
import logo from "../../assets/logo.png";
function Header() {
    let userName = 'mostafaelkaranshawy0'
    return (
        <div className="header">
            <div className="logo">
                <img src={logo}/>
            </div>
            <div className="nav">
                <ul className="nav-menu">
                    <li>
                        <NavLink to="/home" className="menu-item" onClick>
                            <i className="fa-solid fa-house"></i>
                            <p>Home</p>
                        </NavLink>
                    </li>
                    
                    <li>
                        <NavLink to={`/${userName}/profile`} className="menu-item" onClick>
                            <i className="fa-solid fa-user"></i>
                            <p>Profile</p>
                        </NavLink>
                    </li>
                    
                </ul>

            </div>
        </div>
    )
}
export default Header;