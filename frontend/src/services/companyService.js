const backendURL = 'http://localhost:8080';

export const jobApplications = async (req, res) => {
    try {
        const response = await fetch(`${backendURL}/jobApplications`);
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
