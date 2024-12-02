import React, { useState } from "react";
import { employerSignUp } from "../../services/authServices";
import "./EmployerSignUp.css";

function EmployerSignUp() {
  // Form states
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    companyName: "",
    email: "",
    mobileNumber: "",
    password: "",
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
    <div className="App">
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
            required
          />
        </div>
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
          <label htmlFor="mobileNumber">Mobile Number</label>
          <input
            type="text"
            id="mobileNumber"
            name="mobileNumber"
            value={formData.mobileNumber}
            onChange={handleInputChange}
            required
          />
        </div>
        <div>
          <label htmlFor="email">Bussiness Email</label>
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
        <button type="submit" onClick={employerSignUp}>Sign Up</button>
      </form>
      <p className="logIn">
        Already have an account? <a href="/login">Log in</a>
      </p>
    </div>
  );
}

export default EmployerSignUp;
