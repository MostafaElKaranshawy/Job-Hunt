import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { resretPasswordRequest } from "../../services/authServices";

import "./PasswordResetRequestForm.css";
function PasswordResetRequestForm() {
  const [email, setEmail] = useState("");
  const [errors, setErrors] = useState({});
  const [successResponse, setSuccessResponse] = useState("");
  const [failureResponse, setFailureResponse] = useState("");

  const validate = () => {
    const newErrors = {};
    if (!email) {
      newErrors.email = "Email is required.";
    } else if (!/^\S+@gmail\.com$/.test(email)) {
      newErrors.email = "Email must be a valid Gmail address (e.g., example@gmail.com).";
    }
    return newErrors;
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEmail(value);
    setSuccessResponse("");
    setFailureResponse("");
    if (errors[name]) setErrors({ ...errors, [name]: "" });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newErrors = validate();
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }
    const response = await resretPasswordRequest(email); // Ensure this function is implemented properly
    if (response.success) {
      setSuccessResponse(response.message);
      setFailureResponse("");
      setEmail(""); // Reset the input
    } else {
      setFailureResponse(response.message);
      setSuccessResponse("");
    }
  };

  return (
    <div className="reset-password-request-form">
      <h2>Reset Your Password</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="email"
          placeholder="Enter your email"
          value={email}
          onChange={handleInputChange}
          className={errors.email ? "error-input" : ""}
        />
        {errors.email && <span className="error" style={{ color: "red" }}>{errors.email}</span>}
        {successResponse && <p className="success-message">{successResponse}</p>}
        {failureResponse && <p className="error-message">{failureResponse}</p>}
        <button type="submit">Send Reset Link</button>
      </form>
    </div>
  );
}

export default PasswordResetRequestForm;
