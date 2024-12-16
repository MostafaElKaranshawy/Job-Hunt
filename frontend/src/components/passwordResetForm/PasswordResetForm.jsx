import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { resetPassword } from "../../services/authServices";
import "./PasswordResetForm.css";

function PasswordResetForm() {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const location = useLocation();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Ensure passwords match
    if (newPassword !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }

    // Get token from URL query parameter at submit
    const queryParams = new URLSearchParams(location.search);
    const tokenFromUrl = queryParams.get("token");

    if (!tokenFromUrl) {
      alert("No token found in the URL.");
      navigate("/error"); // Redirect to error page or show a message
      return;
    }

    try {
      await resetPassword(tokenFromUrl, newPassword);
      navigate("/login"); 
    } catch (error) {
      console.error("Password reset failed:", error);
      alert("Password reset failed. Please try again.");
    }
  };

  return (
    <div className="password-reset-form-container">
      <h1>Reset Password</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="password">New Password</label>
          <input
            type="password"
            id="password"
            name="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="confirmPassword">Confirm Password</label>
          <input
            type="password"
            id="confirmPassword"
            name="confirmPassword"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Reset Password</button>
      </form>
    </div>
  );
}

export default PasswordResetForm;
