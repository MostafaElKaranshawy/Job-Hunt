const backendURL = 'http://localhost:8080';

export const acceptApplication = async (applicationId) => {
    console.log(applicationId);
    try {
        const response = await fetch(`${backendURL}/job/acceptApplication/${applicationId}`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ applicationId }),  // Pass the applicationId to the backend
        });
        const data = await response.json();
        if (response.ok) {
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
        const response = await fetch(`${backendURL}/job/rejectApplication/${applicationId}`, {
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
export const getApplications = async (jobId) => {
    try {
      const response = await fetch(
        `http://localhost:8080/company/jobs/${jobId}`,
        {
          credentials: "include", // Include credentials for cookies/auth
        }
      );
  
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
  
      const data = await response.json();
      console.log("Job applications data:", data);
      return data;
    } catch (error) {
      console.error("Error fetching job applications:", error);
    }
  };