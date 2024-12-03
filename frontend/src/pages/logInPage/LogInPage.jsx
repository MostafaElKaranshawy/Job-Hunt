import React from "react";
import { Link } from "react-router-dom";
import UserLogIn from "../../components/userLogIn/UserLogIn";
import "./LogInPage.css";

function LogInPage() {
    return (
        <>
        <div className="login-page">
            <UserLogIn />
            <div className="signup-link">
                <p>Don't have an account?</p>
                <Link to="/">Sign up</Link>
            </div>
        </div>
        </>
    );
}
export default LogInPage;