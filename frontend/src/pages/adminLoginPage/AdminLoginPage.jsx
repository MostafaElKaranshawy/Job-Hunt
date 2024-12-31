import React, { useState } from "react";
import { adminLogin } from "../../services/authServices";
import "./AdminLoginPage.css";

function AdminLoginPage() {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
    });

    const [errors, setErrors] = useState({});
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
        setErrors((prevErrors) => ({ ...prevErrors, [name]: "" })); 
        errors.failureMessage && setErrors((prevErrors) => ({ ...prevErrors, failureMessage: "" }));
        errors.successMessage && setErrors((prevErrors) => ({ ...prevErrors, successMessage: "" }));
    };

    const validate = () => {
        const newErrors = {};
        if (!formData.username) {
            newErrors.username = "Username is required.";
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
            return;
        }

        try {
            console.log(formData); 

            const response = await adminLogin(formData);
            if (response.success === true) {
                setErrors({});
                console.log(response);
                window.location.href = '/home'; 
                //change the path to the admin home page
            } else {
                setErrors((prevErrors) => ({
                    ...prevErrors,
                    failureMessage: response.message,
                }));
            }
        } catch (error) {
            console.error("Login failed:", error);
            setErrors((prevErrors) => ({
                ...prevErrors,
                failureMessage: "An error occurred. Please try again.",
            }));
        }
    };

    return (
        <div className="login-page">
            <div className="login-container">
                <h1>Admin Login Page</h1>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="username">Username</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleInputChange}
                            placeholder="ex.Mohamed"
                            className={errors.username ? "error-input" : ""}
                        />
                        {errors.username && <p className="error">{errors.username}</p>}
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

                    <button type="submit" className="send-button">
                        Log In
                    </button>
                </form>
                {errors.failureMessage && <p className="error-message">{errors.failureMessage}</p>}
            </div>
        </div>
    );
}

export default AdminLoginPage;
