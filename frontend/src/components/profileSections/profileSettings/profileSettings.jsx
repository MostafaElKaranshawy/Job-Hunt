import React, {useState} from "react";
import './profileSettings.css';
import { changePassword } from "../../../services/userProfileService";
import MessageBox from "../../messageBox/messageBox";
import { useParams } from "react-router-dom";

export default function ProfileSetting() {
    const { userName } = useParams();
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [errors, setErrors] = useState({});
    const [showMessaegBox, setShowMessaegBox] = useState(false);
    const [message, setMessage] = useState('');
    const validatePassword = () => {
        let errors = {};
        let isValid = true;

        if (!oldPassword) {
            isValid = false;
            errors["oldPassword"] = "Please enter your old password.";
        }

        if (!newPassword) {
            isValid = false;
            errors["newPassword"] = "Please enter your new password.";
        }

        if (!newPassword) {
            errors["newPassword"] = "Password is required.";
            isValid = false;
        } else if (newPassword.length < 8) {
            errors["newPassword"] = "Password must be at least 8 characters long.";
            isValid = false;
        } else if (!/[A-Z]/.test(newPassword)) {
            errors["newPassword"] = "Password must contain at least one uppercase letter.";
            isValid = false;
        } else if (!/[a-z]/.test(newPassword)) {
            errors["newPassword"] = "Password must contain at least one lowercase letter.";
            isValid = false;
        } else if (!/[0-9]/.test(newPassword)) {
            errors["newPassword"] = "Password must contain at least one number.";
            isValid = false;
        } else if (!/[!@#$%^&*(),.?":{}|<>]/.test(newPassword)) {
            errors["newPassword"] = "Password must contain at least one special character.";
            isValid = false;
        }  else if (newPassword.length > 30) {
            errors["newPassword"] = "Password must be at most 30 characters";
            isValid = false;
        }
        setErrors(errors);
        return isValid;
    }
    const saveChanges = async () => {
        let isValid = validatePassword();
        if(!isValid) return;
        const res = changePassword(userName, {oldPassword, newPassword});
        if (res === "ok") {
            setOldPassword('');
            setNewPassword('');
            setMessage("Password changed successfully");
            setShowMessaegBox(true);
        }
        else {
            setMessage(res);
            setErrors({ "oldPassword": "Old password is incorrect." });
            setShowMessaegBox(true);
            console.log(res);
        }
    }
    return (
        <div className="profile-settings">
            {showMessaegBox && message && <MessageBox message={message} setShowMessaegBox={setShowMessaegBox}/>}
            <div className="settings-header">
                Settings
            </div>
            <div className="settings-body">
                <div className="section">
                    <div className="section-header">
                        <div className="section-name">Change Password</div>
                    </div>
                    <div className="section-fields">
                        <div className="section-field">
                            <div className="field-name">Old Password</div>
                            <div className="input-container">
                                <input
                                    type="password"
                                    name="oldPassword"
                                    value={oldPassword}
                                    className={`field-value editable ${errors["oldPassword"] ? "error-field" : ""}`}
                                    onChange={(e) => {
                                        setOldPassword(e.target.value)
                                        setErrors({ ...errors, "oldPassword": ""})
                                    }}
                                />
                                <div className="error">{errors["oldPassword"]}</div>
                            </div>
                        </div>
                        <div className="section-field">
                            <div className="field-name">New Password</div>
                            <div className="input-container">
                                <input
                                    type="password"
                                    name="newPassword"
                                    value={newPassword}
                                    className={`field-value editable ${errors["newPassword"] ? "error-field" : ""}`}
                                    onChange={(e) => {
                                        setNewPassword(e.target.value)
                                        setErrors({ ...errors, "newPassword": ""})
                                    }}
                                />
                                <div className="error">{errors["newPassword"]}</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="save-button" onClick={saveChanges}>
                    Save
                </div>

            </div>
        </div>
    );
}