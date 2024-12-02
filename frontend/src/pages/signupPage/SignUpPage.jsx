import React, { useState } from "react";
import GoogleOAuth from "../../components/googleOAuthLogIn/GoogleOAuthLogIn";
import EmployeeSignUp from "../../components/employeeSignUp/EmployeeSignUp";
import EmployerSignUp from "../../components/employerSignUp/EmployerSignUp";
import "./SignUpPage.css";

function SignUpPage() {
    const [isEmployer, setIsEmployer] = useState(false);

    return (
        <>
            <button
                className="employer-button"
                onClick={() => setIsEmployer(!isEmployer)}
            >
                {isEmployer ? "Switch to Employee" : "Switch to Employer"}
            </button>
            {isEmployer ? (
                <div className="employer-signup">
                    <h2>Employer Sign-Up Form</h2>
                    <EmployerSignUp />
                </div>
            ) : (
                <div className="signup-container">
                    <EmployeeSignUp />
                    <div className="orLineContainer">
                       <hr />
                       <span> or </span>
                       <hr />
                    </div>
                    <GoogleOAuth />
                </div>
            )}
        </>
    );
}

export default SignUpPage;
