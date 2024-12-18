import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { resetPassword } from "../../services/authServices";
import "./PasswordResetForm.css";

function PasswordResetForm() {
  const location = useLocation();
  const [formData, setFormData] = useState({
    password: "",
    confirmPassword: "",
  });
  const [errors, setErrors] = useState({});
  const [successResponse, setSuccessResponse] = useState("");
  const [failureResponse, setFailureResponse] = useState("");

  const validate = () =>{
    const newErrors = {};
    if (!formData.password) {
      newErrors.password = "Password is required.";
  } else if (formData.password.length < 8) {
      newErrors.password = "Password must be at least 8 characters long.";
  } else if (!/[A-Z]/.test(formData.password)) {
      newErrors.password = "Password must contain at least one uppercase letter.";
  } else if (!/[a-z]/.test(formData.password)) {
      newErrors.password = "Password must contain at least one lowercase letter.";
  } else if (!/[0-9]/.test(formData.password)) {
      newErrors.password = "Password must contain at least one number.";
  } else if (!/[!@#$%^&*(),.?":{}|<>]/.test(formData.password)) {
      newErrors.password = "Password must contain at least one special character.";
  }  else if (formData.password.length > 30) {
      newErrors.password = "Password must be at most 30 characters";
  }
  
    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match.";
    }
    return newErrors;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      const queryParams = new URLSearchParams(location.search);
      const tokenFromUrl = queryParams.get("token");
    if (!tokenFromUrl) {
      alert("No token found in the URL.");
      return;
    }
    setErrors({});
     const response = await resetPassword(tokenFromUrl, formData.password);
     if(response.success){
        setSuccessResponse(response.message);
        setFailureResponse("");
     }
      else{
          setFailureResponse(response.message);
          setSuccessResponse("");
      }  
  };
};

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
    setErrors({ ...errors, [name]: "" });
  };
  return (
    <div className="password-reset-form-container">
      <h1>Reset Password</h1>
      <form>
        <div>
          <label htmlFor="password">New Password</label>
          <input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={handleInputChange}
          />
        </div>
        {errors.password && <p className="error">{errors.password}</p>}
        <div>
          <label htmlFor="confirmPassword">Confirm Password</label>
          <input
            type="password"
            id="confirmPassword"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleInputChange}
          />
        </div>
        {errors.confirmPassword && <p className="error">{errors.confirmPassword}</p>}
        <button onClick={handleSubmit}>Reset Password</button>
        {
        successResponse && <p className="success-message">{successResponse}</p>
      }
      {
        failureResponse && <p className="error-message">{failureResponse}</p>
      }
      </form>
    </div>
  );
}

export default PasswordResetForm;
