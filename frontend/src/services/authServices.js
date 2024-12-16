const bcrypt = require('bcryptjs');

const hashPassword = async (password) => {
  // const salt = await bcrypt.genSalt(10); // Generates a salt with 10 rounds
  // const hashedPassword = await bcrypt.hash(password, salt); // Hashes the password with the salt
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
    console.log(data.message);
  } else {
    const errorData = await response.json();
    console.error(errorData.message);
  }
};

export const employeeSignUp = async (formData) => {
  formData.password = hashPassword(formData.password);
  const response = await fetch(`${apiUrl}/auth/signup/applicant`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(formData),
  });
  if (response.ok) {
    const data = await response.json();
    console.log( data.message);
  } else {
    const errorData = await response.json();
    console.error( errorData.message);
  }
};

export const googleSignUp = async (credentialResponse) => {
  const response = await fetch(`${apiUrl}/auth/signup/applicant/google`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({googleToken:credentialResponse.credential}),
  });
  if (response.ok) {
    const data = await response.json();
    console.log(data.message);
  } else {
    const errorData = await response.json();
    console.error(errorData.message);
  }
};

export const googleLogIn = async (credentialResponse) => {
  const response = await fetch(`${apiUrl}/auth/login/applicant/google`, {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({googleToken:credentialResponse.credential}),
  });
  if (response.ok) {
    const data = await response.json();
    console.log(data.username);
  } else {
    const errorData = await response.json();
    console.error(errorData.message);
  }
};

export const logIn = async (formData) => {
  formData.password =await hashPassword(formData.password);
  try {
    const response = await fetch(`${apiUrl}/auth/login`, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });

    if (response.ok) {
      const data = await response.json();   //this is the username
      console.log(data.username);

      const username = 'testuser'; 
      const res2 = await fetch(`${apiUrl}/user/${username}`, {
        method: "GET",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
      });
      const userData = await res2.json();
      console.log(userData);
    } else {
      const errorData = await response.json(); 
      throw new Error(errorData.message); 
    }
  } catch (error) {
    console.error(error.message);
  }
};

export const resretPasswordRequest = async (email) => {
  const response = await fetch(`${apiUrl}/auth/reset-password-request`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email }),
  });

  if (response.ok) {
    const data = await response.json();
    console.log(data.message);
  } else {
    const errorData = await response.json();
    console.error(errorData.message);
  }
}

export const resetPassword = async (resetToken, newPassword) => {
  const response = await fetch(`${apiUrl}/auth/reset-password?resetToken=${resetToken}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ password: newPassword }), // Send only newPassword in the request body
  });

  if (response.ok) {
    const data = await response.json();
    console.log(data.message);
    // return data.message;
  } else {
    const errorData = await response.json();
    console.error( errorData.message);
  }


};
