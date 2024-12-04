import React, { useState } from "react";
import { employerSignUp } from "../../services/authServices";
import { Link } from "react-router-dom";
import "./EmployerSignUp.css";
function EmployerSignUp() {
  // Form states
  const [formData, setFormData] = useState({
    companyName: "",
    email: "",
    password: "",
  });
  const [errors, setErrors] = useState({});
  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
    setErrors({ ...errors, [name]: "" });
  };

  // Validation function
  const validate = () => {
    const newErrors = {};
    if (!formData.companyName) newErrors.companyName = "Company name is required.";
    if (!formData.email) {
      newErrors.email = "Email is required.";
    } else if (!/^\S+@gmail\.com$/.test(formData.email)) {
      newErrors.email =
        "Email must be a valid Gmail address (e.g., example@gmail.com).";
    }
    if (!formData.password) {
      newErrors.password = "Password is required.";
    } else if (formData.password.length < 6) {
      newErrors.password = "Password must be at least 6 characters.";
    }
    return newErrors;
  };
  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      setErrors({});
      employerSignUp(formData);
    }
  };
  return (
    <div className="signup-container">
      <h1>Company Sign Up</h1>
      <form>
        <div>
          <label htmlFor="companyName">Company Name</label>
          <input
            type="text"
            id="companyName"
            name="companyName"
            value={formData.companyName}
            onChange={handleInputChange}
            className={errors.companyName ? "error-input" : ""}
          />
          {errors.companyName && (
            <p className="error">{errors.companyName}</p>
          )}
        </div>
        <div>
          <label htmlFor="email">Business Email</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleInputChange}
            placeholder="example@gmail.com"
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
          {errors.password && (
            <p className="error">{errors.password}</p>
          )}
        </div>
      </form>
      <button type="submit" onClick={handleSubmit} className="send-button">
          Sign Up
        </button>
      <p className="logIn"> Already have an account? <Link to="/login" className="link">log in</Link></p>
      
    </div>
  );
}

export default EmployerSignUp;
