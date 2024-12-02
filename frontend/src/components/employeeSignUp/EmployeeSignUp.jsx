import React, { useState } from "react";
import { employeeSignUp } from "../../services/authServices";
import "./EmployeeSignUp.css";

function EmployeeSignUp() {

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    birthdate: "",
    password: "",
    confirmPassword: "",
  });

  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  return (
    <div className="signup-container">
          <h1> Create your account and start applying for jobs </h1>
          <form>
            <div>
              <label htmlFor="firstName">first name</label>
              <input
                type="text"
                id="firstName"
                name="firstName"
                value={formData.firstName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="lastName">last name</label>
              <input
                type="text"
                id="lastName"
                name="lastName"
                value={formData.lastName}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="birthdate">Birthdate</label>
              <input
                type="date"
                id="birthdate"
                name="birthdate"
                value={formData.birthdate}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="password">Password</label>
              <input
                type="password"
                id="password"
                name="password"
                value={formData.password}
                onChange={handleInputChange}
                required
              />
            </div>
            <div>
              <label htmlFor="confirmPassword">Confirm Password</label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleInputChange}
                required
              />
            </div>
            <button onClick={()=>employeeSignUp(formData)}>Sign Up</button>
          </form>
          <p className="login">
            Already have an account? <a href="/login">Log in</a>
          </p>
        </div>
  );
}

export default EmployeeSignUp;
