const hashPassword = (password) => {
  // return CryptoJS.SHA256(password).toString(CryptoJS.enc.Hex); // Generate SHA-256 hash
  return password;
};

const apiUrl = import.meta.env.VITE_API_URL;

export const employerSignUp = async (formData) => {
  formData.password = hashPassword(formData.password);
  const response = await fetch(`${apiUrl}/auth/signup/company`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(formData),
  });
  if (response.ok) {
    const data = await response.json();
    console.log("Backend Response:", data);
  } else {
    console.error("Backend responded with an error:", response.status);
  }
};
export const employeeSignUp = async (formData) => {
  formData.password = hashPassword(formData.password);
  console.log("Form Data:", formData);
  const response = await fetch(`${apiUrl}/auth/signup/applicant`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(formData),
  });
  if (response.ok) {
    const data = await response.json();
    console.log("Backend Response:", data);
  } else {
    console.error("Backend responded with an error:", response.status);
  }
};
export const googleSignUp = async (credentialResponse) => {
  console.log("Google Credential:", credentialResponse);
  const response = await fetch(`${apiUrl}/auth/signup/applicant/google`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({googleToken:credentialResponse.credential, clientId: credentialResponse.clientId}),
  });
  if (response.ok) {
    const data = await response.json();
    console.log("user Signed up successfully with Google:", data);
  } else {
    console.error("User could not sign Up:", response.status);
  }
};
export const googleLogIn = async (credentialResponse) => {
  console.log("Google Credential:", credentialResponse);
  const response = await fetch(`${apiUrl}/auth/login/applicant/google`, {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
    // body: JSON.stringify(credentialResponse.credential),
    body: JSON.stringify({googleToken:credentialResponse.credential, clientId: credentialResponse.clientId}),
  });
  if (response.ok) {
    const data = await response.json();
    console.log("user Logged in successfully with Google:", data);
  } else {
    console.error("User could not log in:", response.status);
  }
};
export const logIn = async (formData) => {
  formData.password = hashPassword(formData.password);
  const response = await fetch(`${apiUrl}/auth/login`, {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(formData),
  });
  if (response.ok) {
    const data = await response.text();
    console.log("User Logged in successfully:", data);
    const username = 'testuser';
    const res2 = await fetch(`${apiUrl}/user/${username}`, {
      method: "GET",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
    });
    
  } else {
    console.error("User could not log in:", response.status);
  }
};
export const resetPassword = async (resetToken, newPassword) => {
  // Append the resetToken as a query parameter
  const response = await fetch(`${apiUrl}/auth/reset-password?resetToken=${resetToken}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ password: newPassword }), // Send only newPassword in the request body
  });

  if (response.ok) {
    const data = await response.json();
    console.log("Password reset successful:", data);
    return data; // Return response data if needed
  } else {
    console.error("Backend responded with an error:", response.status);
    throw new Error("Failed to reset password"); // Optionally throw an error to handle in UI
  }
};
