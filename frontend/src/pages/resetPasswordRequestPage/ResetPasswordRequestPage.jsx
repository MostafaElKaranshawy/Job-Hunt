import React, { useState } from "react";
import ResetPasswordRequestForm from "../../components/PasswordResetRequestForm/PasswordResetRequestForm";
import "./ResetPasswordRequestPage.css";

function ResetPasswordRequestPage(){
    return(
        <div className="t">
            <div className="reset-password-request-page-container">
                <ResetPasswordRequestForm />
            </div>
        </div>
    );
}
export default ResetPasswordRequestPage;