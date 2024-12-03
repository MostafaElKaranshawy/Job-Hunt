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
  const response = await fetch("http://localhost:8080/auth/google/signUp", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(credentialResponse.credential),
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
  const response = await fetch("http://localhost:8080/auth/google/logIn", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(credentialResponse.credential),
  });
  if (response.ok) {
    const data = await response.json();
    console.log("user Logged in successfully with Google:", data);
  } else {
    console.error("User could not log in:", response.status);
  }
};
export const logIn = async (formData) => {
  const response = await fetch("http://localhost:8080/auth/logIn", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(formData),
  });
  if (response.ok) {
    const data = await response.json();
    console.log("User Logged in successfully:", data);
  } else {
    console.error("User could not log in:", response.status);
  }
};