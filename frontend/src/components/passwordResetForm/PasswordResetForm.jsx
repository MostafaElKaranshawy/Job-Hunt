import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { resetPassword } from "../../services/authServices";
import "./PasswordResetForm.css";

function PasswordResetForm() {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [token, setToken] = useState("");
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    // Get token from URL query parameter
    const queryParams = new URLSearchParams(location.search);
    const tokenFromUrl = queryParams.get("token");
    if (tokenFromUrl) {
      setToken(tokenFromUrl); // Set token from URL
    } else {
      // Handle the case where no token is found
      console.error("No token found in URL.");
      navigate("/error"); // Redirect to error page or show a message
    }
  }, [location.search, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Ensure passwords match
    if (newPassword !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }
    console.log("Resetting password...");
    console.log("Token: ", token);
    console.log("New Password : ", newPassword  );
    // Call the backend service to reset the password
    const response = await resetPassword(token, newPassword);
    if (response.ok) {
      console.log("Password reset successful");
      navigate("/login"); // Redirect to login page
    } else {
      console.error("Failed to reset password");
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
