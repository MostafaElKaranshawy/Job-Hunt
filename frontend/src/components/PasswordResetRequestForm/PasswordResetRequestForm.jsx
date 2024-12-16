import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { resretPasswordRequest } from "../../services/authServices";
function PasswordResetRequestForm  () {
  const [email, setEmail] = useState("");
  const [errors, setErrors] = useState({});
  const validate = ()=> {
    const newErrors = {};
    if (!email) {
      newErrors.email = "Email is required.";
    } else if(!/^\S+@gmail\.com$/.test(email))
     {
      newErrors.email = "Email must be a valid Gmail address (e.g., example@gmail.com).";
    }
    return newErrors;
  }
 
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEmail(value);
    errors[name] && setErrors({ ...errors, [name]: "" });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newErrors = validate();
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }
    resretPasswordRequest(email);     
  };

  return (
    <div>
      <h2>Reset Your Password</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Enter your email"
          value={email}
          onChange={handleInputChange}
        />
        {errors.email && <span style={{ color: "red" }}>{errors.email}</span>}
        <button type="submit">Send Reset Link</button>
      </form>
    </div>
  );
};

export default PasswordResetRequestForm;
