import { func } from "prop-types";
import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./ResetPasswordPage.css";
import PasswordResetForm from "../../components/passwordResetForm/PasswordResetForm";
function ResetPasswordPage(){
    return(
        <div className="t">
            <div className="reset-password-page-container">
                <PasswordResetForm />
            </div>
        </div>
    );
}
export default ResetPasswordPage;