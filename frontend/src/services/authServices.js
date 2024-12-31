const apiUrl = 'http://localhost:8080';
const ERROR_MESSAGE = "Sign-up failed. Please try again.";
const ERROR_MESSAGE_LOGIN = "Login failed. Please try again.";

export const employerSignUp = async (formData) => {
    try {
        const response = await fetch(`${apiUrl}/auth/signup/company`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData),
        });
        const data = await response.json();
        if (response.ok) {
            console.log(data.message);
            return { success: true, message: data.message };
        } else {
            console.error(data.message);
            return { success: false, message: data.message };
        }
    } catch (error) {
        console.error(error);
        return { success: false, message: ERROR_MESSAGE };
    }
};
export const employeeSignUp = async (formData) => {
    try {
        const response = await fetch(`${apiUrl}/auth/signup/applicant`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData),
        });

        const data = await response.json();
        if (response.ok) {
            console.log(data.message);
            return { success: true, message: data.message };
        } else {
            console.error(data.message);
            return { success: false, message: data.message };
        }
    } catch (error) {
        console.error(error);
        return { success: false, message: ERROR_MESSAGE };
    }
};

export const googleSignUp = async (credentialResponse) => {
    try {
        const response = await fetch(`${apiUrl}/auth/signup/applicant/google`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ googleToken: credentialResponse.credential }),
        });
        const data = await response.json();
        if (response.ok) {
            console.log(data.message);
            return { success: true, message: data.message };
        } else {
            console.error(data.message);
            return { success: false, message: data.message };
        }
    }
    catch (error) {
        console.error(error);
        return { success: false, message: ERROR_MESSAGE };
    }
};

export const googleLogIn = async (credentialResponse) => {
    try {
        const response = await fetch(`${apiUrl}/auth/login/applicant/google`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ googleToken: credentialResponse.credential }),
        });
        const data = await response.json();
        console.log(data);
        if (response.ok) {
            document.cookie = `username=${data.username};`;
            console.log(data.message);
            return { success: true, userType: data.userType, username: data.username };
        } else {
            console.error(data.message);
            return { success: false, message: data.message };
        }

    } catch (error) {
        console.error(error.message);
        return { success: false, message: data.message };
    }
};

export const logIn = async (formData) => {

    console.log(formData);
    try {
        const response = await fetch(`${apiUrl}/auth/login`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData),
        });
        const data = await response.json();
        console.log(data);
        if (response.ok) {
            document.cookie = `username=${data.username};`;
            console.log(data.message);
            return { success: true, userType: data.userType, username: data.username };
        } else {
            console.error(data.message);
            return { success: false, message: data.message };
        }

    } catch (error) {
        console.error(error.message);
        return { success: false, message: data.message };
    }
};

export const adminLogin = async (formData) => {
    console.log(formData);
    try {
        const response = await fetch(`${apiUrl}/auth/admin/login`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData),
        });
        const data = await response.json();
        if (response.ok) {
            document.cookie = `username=${data.username};`;
            //and i return the userType also if you need it
            console.log(data.message);
            return { success: true, message: data.message };
        } else {
            console.error(data.message);
            return { success: false, message: data.message };
        }
    } catch (error) {
        console.error(error);
        return { success: false, message: ERROR_MESSAGE_LOGIN };
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
    const data = await response.json();
    if (response.ok) {
        console.log(data.message);
        return { success: true, message: data.message };
    } else {
        console.error(data.message);
        return { success: false, message: data.message };
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
    const data = await response.json();
    if (response.ok) {
        console.log(data.message);
        return { success: true, message: data.message };
    } else {
        console.error(data.message);
        return { success: false, message: data.message };
    }
};
