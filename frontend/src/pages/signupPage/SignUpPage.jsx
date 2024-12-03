import React, { useState } from "react";
import { Link } from "react-router-dom";
import GoogleOAuth from "../../components/googleOAuthLogIn/GoogleOAuthLogIn";
import EmployeeSignUp from "../../components/employeeSignUp/EmployeeSignUp";
import EmployerSignUp from "../../components/employerSignUp/EmployerSignUp";
import "./SignUpPage.css";

function SignUpPage() {
    const [isEmployer, setIsEmployer] = useState(false);

    return (
        <>
        <div className="signup-page">
            <button
                className="employer-button"
                onClick={() => setIsEmployer(!isEmployer)}
            >
                {isEmployer ? "Switch to Employee" : "Switch to Employer"}
            </button>
            {isEmployer ? (
                <div className="employer-signup">
                    <EmployerSignUp />
                </div>
            ) : (
                <div className="employee-signup">
                    <EmployeeSignUp />
                    <div className="orLineContainer">
                       <hr />
                       <span> or </span>
                       <hr />
                    </div>
                    <GoogleOAuth mode="signup"/>
                </div>
          
            )}
         </div>
        </>
    );
}

export default SignUpPage;
