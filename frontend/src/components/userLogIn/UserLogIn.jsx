import React, { useState } from "react";
import { logIn } from "../../services/authServices";
import { Link } from "react-router-dom";
import "./UserLogIn.css";

function UserLogIn() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [errors, setErrors] = useState({});

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
    setErrors({ ...errors, [name]: "" }); 
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.email) {
      newErrors.email = "Email is required.";
    }else if (!/^\S+@gmail\.com$/.test(formData.email)) {
      newErrors.email =
        "Email must be a valid Gmail address (e.g., example@gmail.com).";
    }
    if (!formData.password) {
      newErrors.password = "Password is required.";
    }
    return newErrors;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors); 
    } else {
      logIn(formData);
      setErrors({}); 
    }
  };

  return (
    <div className="login-container">
      <h1>User Log In</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input
            type="text"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleInputChange}
            placeholder="example@gmail.com"
            className={errors.email ? "error-input" : ""}
          />
          {errors.email && <p className="error">{errors.email}</p>}
        </div>
        <div className="form-group">
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
        <button type="submit" className="login-button">
          Log In
        </button>
      </form>
      <p>Don't have an account?</p>
      <Link to="/">Sign up</Link>
    </div>
  );
}

export default UserLogIn;
