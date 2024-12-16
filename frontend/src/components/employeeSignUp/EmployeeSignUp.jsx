import React, { useState } from "react";
import { employeeSignUp } from "../../services/authServices";
import { Link } from "react-router-dom";
import "./EmployeeSignUp.css";

function EmployeeSignUp() {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
  });

  const [successResponse, setSuccessResponse] = useState("");
  const [failureResponse, setFailureResponse] = useState("");

  const [errors, setErrors] = useState({}); // State for validation errors

  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
    errors[name] && setErrors({ ...errors, [name]: "" });
  };

  // Validation function
  const validate = () => {
    const newErrors = {};
    if (!formData.firstName || formData.firstName.trim() === "") {
      newErrors.firstName = "First name is required.";
  } else if (formData.firstName.length > 30) {
      newErrors.firstName = "First name must not exceed 30 characters.";
  } else if (/\d/.test(formData.firstName)) {
      newErrors.firstName = "First name must not contain numbers.";
  } else if (/\s/.test(formData.firstName)) {
    newErrors.firstName = "First name must not contain spaces.";
  }
  
  if (!formData.lastName || formData.lastName.trim() === "") {
      newErrors.lastName = "Last name is required.";
  } else if (formData.lastName.length > 30) {
      newErrors.lastName = "Last name must not exceed 30 characters.";
  } else if (/\d/.test(formData.lastName)) {
      newErrors.lastName = "Last name must not contain numbers.";
  } else if (/\s/.test(formData.lastName)) {
    newErrors.lastName = "Last name must not contain spaces.";
  }
  
    if (!formData.email) {
      newErrors.email = "Email is required.";
    } else if(!/^\S+@gmail\.com$/.test(formData.email))
     {
      newErrors.email = "Email must be a valid Gmail address (e.g., example@gmail.com).";
    }
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
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      setErrors({});
     const response = await employeeSignUp(formData);
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

  return (
    <div className="signup-component-container">
      <h1>Sign up</h1>
      <form>
        <div className="name-container">
          <div>
          <label htmlFor="firstName">First Name</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            value={formData.firstName}
            onChange={handleInputChange}
            className={errors.firstName ? "error-input" : ""}
          />
          {errors.firstName && <p className="error">{errors.firstName}</p>}
          </div>
          <div>
          <label htmlFor="lastName">Last Name</label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            value={formData.lastName}
            onChange={handleInputChange}
            className={errors.lastName ? "error-input" : ""}
          />
          {errors.lastName && <p className="error">{errors.lastName}</p>}
          </div>
        </div>
        <div>
          <label htmlFor="email">Email</label>
          <input
            type="text"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleInputChange}
            className={errors.email ? "error-input" : ""}
          />
          {errors.email && <p className="error">{errors.email}</p>}
        </div>
        <div>
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={handleInputChange}
            className={errors.password ? "error-input" : ""}
          />
          {errors.password && <p className="error">{errors.password}</p>}
        </div>
        <div>
          <label htmlFor="confirmPassword">Confirm Password</label>
          <input
            type="password"
            id="confirmPassword"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={handleInputChange}
            className={errors.confirmPassword ? "error-input" : ""}
          />
          {errors.confirmPassword && (
            <p className="error">{errors.confirmPassword}</p>
          )}
        </div>
      </form>
      <button type="submit" className="send-button" onClick={handleSubmit}>Sign Up</button>
      <p className="login">
        Already have an account? <Link to="/login" className="link">Log in</Link>
      </p>
      {
        successResponse && <p className="success-message">{successResponse}</p>
      }
      {
        failureResponse && <p className="error-message">{failureResponse}</p>
      }
    </div>
  );
}

export default EmployeeSignUp;
