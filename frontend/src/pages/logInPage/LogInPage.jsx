import React from "react";
import UserLogIn from "../../components/userLogIn/UserLogIn";
import GoogleOAuth from "../../components/googleOAuthLogIn/GoogleOAuthLogIn";
import "./LogInPage.css";

function LogInPage() {
    return (
        <div className="t">
        <div className="login-page">
            <div className="user-login">
            <UserLogIn />
            <div className="orLineContainer">
                <hr />
                <span> or </span>
                <hr />
            </div>
            <GoogleOAuth mode="login"/>
            </div>
        </div>
        </div>
    );
}
export default LogInPage;