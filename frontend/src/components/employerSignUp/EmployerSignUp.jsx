import React, { useState } from "react";
import { employerSignUp } from "../../services/authServices";
import "./EmployerSignUp.css";
import PhoneInput from "react-phone-input-2";
import "react-phone-input-2/lib/style.css";
import { isValidPhoneNumber} from "libphonenumber-js";
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
  const [errors, setErrors] = useState({});
  const [selectedCountry, setSelectedCountry] = useState("eg");


   
  const handleCountryChange = (country) => {
    setSelectedCountry(country); // Update the selected country
  };
  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
    setErrors({ ...errors, [name]: "" });
  };
  const handlePhoneChange = (value) => {
    setFormData({
      ...formData,
      mobileNumber: value,
    });
    setErrors({ ...errors, mobileNumber: "" });
  };

  // Validation function
  const validate = () => {
    const newErrors = {};
    if (!formData.companyName) newErrors.companyName = "Company name is required.";
    if (!formData.firstName) newErrors.firstName = "First name is required.";
    if (!formData.lastName) newErrors.lastName = "Last name is required.";
    if (!formData.email) {
      newErrors.email = "Email is required.";
    } else if (!/^\S+@gmail\.com$/.test(formData.email)) {
      newErrors.email =
        "Email must be a valid Gmail address (e.g., example@gmail.com).";
    }
    if (!formData.mobileNumber) {
      newErrors.mobileNumber = "Mobile number is required.";
    } else {
      try {
        console.log(selectedCountry);
        console.log(formData.mobileNumber);
        if (!isValidPhoneNumber(formData.mobileNumber, selectedCountry)) {
          newErrors.mobileNumber = "Mobile number is invalid.";
        }
      } catch (error) {
        newErrors.mobileNumber = "Mobile number is invalid.";
      }
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
          <label htmlFor="firstName">First Name</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            value={formData.firstName}
            onChange={handleInputChange}
            className={errors.firstName ? "error-input" : ""}
          />
          {errors.firstName && (
            <p className="error">{errors.firstName}</p>
          )}
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
          {errors.lastName && (
            <p className="error">{errors.lastName}</p>
          )}
        </div>

        <div>
          <label htmlFor="mobileNumber">Mobile Number</label>
          <PhoneInput 
            country={selectedCountry}
            value={formData.mobileNumber}
            onChange={handlePhoneChange}
            onCountryChange={handleCountryChange}
            enableSearch={true}
            className={errors.mobileNumber ? "error-input" : ""}
          />
          {errors.mobileNumber && (
            <p className="error">{errors.mobileNumber}</p>
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

        <button type="submit" onClick={handleSubmit}>
          Sign Up
        </button>
      </form>
      <p className="logIn">
        Already have an account? <a href="/login">Log in</a>
      </p>
    </div>
  );
}

export default EmployerSignUp;
