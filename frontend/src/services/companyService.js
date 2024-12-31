const backendURL = 'http://localhost:8080';

export const acceptApplication = async (applicationId) => {
    try {
        const response = await fetch(`${backendURL}/company/acceptApplication`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ applicationId }),  // Pass the applicationId to the backend
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
        return { success: false, message: 'An error occurred while accepting the application.' };
    }
};
export const rejectApplication = async (applicationId) => {
    try {
        const response = await fetch(`${backendURL}/company/rejectApplication`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ applicationId }), 
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
        return { success: false, message: 'An error occurred while rejecting the application.' }; 
    }
};