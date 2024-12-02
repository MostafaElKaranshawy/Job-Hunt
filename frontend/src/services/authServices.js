export const employerSignUp = async (formData) => {
  const response = await fetch("http://localhost:8080/auth/employeer/signUp", {
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
  console.log("Form Data:", formData);
  const response = await fetch("http://localhost:8080/auth/employee/signUp", {
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
  const googleAuthDto = {
    idToken: credentialResponse.credential,
  };
  const response = await fetch("http://localhost:8080/auth/google/signUp", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(googleAuthDto),
  });
  if (response.ok) {
    const data = await response.json();
    console.log("user Signed up successfully with Google:", data);
  } else {
    console.error("User could not sign Up:", response.status);
  }
};
