import React from "react";
import { NavLink, useParams} from "react-router-dom";
import "./header.css";
import logo from "../../assets/logo.png";
function Header() {
    const { userName } = useParams();
    return (
        <div className="header">
            <div className="logo">
                <img src={logo}/>
            </div>
            <div className="nav">
                <ul className="nav-menu">
                    <li>
                        <NavLink to="/home" className="menu-item">
                            <i className="fa-solid fa-house"></i>
                            <p>Home</p>
                        </NavLink>
                    </li>
                    
                    <li>
                        <NavLink to={`/user/${userName}/profile`} className="menu-item">
                            <i className="fa-solid fa-user-tie"></i>
                            <p>Profile</p>
                        </NavLink>
                    </li>
                    
                </ul>

            </div>
        </div>
    )
}
export default Header;