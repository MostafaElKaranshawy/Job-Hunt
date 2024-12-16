import React, { useState } from "react";
import ResetPasswordRequestForm from "../../components/PasswordResetRequestForm/PasswordResetRequestForm";
function ResetPasswordRequestPage(){
    return(
        <div className="reset-password-request-page-container">
            <ResetPasswordRequestForm />
        </div>
    );
}
export default ResetPasswordRequestPage;