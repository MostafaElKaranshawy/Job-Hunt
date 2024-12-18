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
        } else if (!/^\S+@gmail\.com$/.test(formData.email)) {
            newErrors.email =
                "Email must be a valid Gmail address (e.g., example@gmail.com).";
        }
        if (!formData.password) {
            newErrors.password = "Password is required.";
        }
        return newErrors;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
        } else {
            const response = await logIn(formData);
            if (response.success === true) {
                setErrors({});
                window.location.href = '/home';
            }
            else {
                setErrors({ ...errors, failureMessage: response.message });

            }
        }
    };


// import React from 'react';

// const SetCookieExample = () => {
//     const setCookie = () => {
//       const name = "user";
//       const value = "JohnDoe";
//       const days = 7;
  
//       // Calculate expiry date
//       const expiryDate = new Date();
//       expiryDate.setDate(expiryDate.getDate() + days);
  
//       // Set cookie
//       document.cookie = ${name}=${value}; path=/; expires=${expiryDate.toUTCString()};
//       alert("Cookie has been set!");
//     };
  
//     return (
//       <button onClick={setCookie}>Set Cookie</button>
//     );
//   };
  
//   export default SetCookieExample;


    return (
        <div className="login-container">
            <h1>Log in</h1>
            <form>
                <div>
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
            </form>
            <button type="submit" className="send-button" onClick={handleSubmit}>
                Log In
            </button>
            {errors.failureMessage && <p className="error-message">{errors.failureMessage}</p>}
            <p className="login">Don't have an account? <Link to="/" className="link">Sign up</Link></p>
            <p className="login">Forgot your password? <Link to="/reset-password-request" className="link">Reset it</Link></p>

        </div>
    );
}

export default UserLogIn;
