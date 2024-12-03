import React from "react";
import UserLogIn from "../../components/userLogIn/UserLogIn";
import GoogleOAuth from "../../components/googleOAuthLogIn/GoogleOAuthLogIn";
import "./LogInPage.css";

function LogInPage() {
    return (
        <>
        <div className="login-page">
            <UserLogIn />
            <div className="orLineContainer">
                <hr />
                <span> or </span>
                <hr />
            </div>
            <GoogleOAuth mode="login"/>
        </div>
        </>
    );
}
export default LogInPage;