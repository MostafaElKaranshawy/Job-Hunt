const backendURL = import.meta.env.VITE_BACKEND_URL;

async function getUserProfile(userName) {
    try{
        const url = `${backendURL}/users/${userName}/profile`;
        const response = await fetch(url, {
            method: 'GET', // Specify the method here
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }

        const json = await response.json();
        return json;
    } catch (error) {
        console.error(error.message);
    }
}

async function editUserProfile(userName, userData) {
    const url = `${backendURL}/users/${userName}/profile`;
    try {
        const response = await fetch(url, {
            method: 'PUT', // Specify the method here
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }
  } catch (error) {
    console.error(error.message);
  }
}

async function handleCustomSectionOperation(
    operation,
    sectionType,
    sectionData = null,
    id
) {
    try {
        let url = ''
        if(operation == 'GET' || operation == 'POST'){
            url = `${backendURL}/users/${id}/profile/${sectionType.toLowerCase()}`;
        }
        else if(operation == 'PUT' || operation == 'DELETE'){
            url = `${backendURL}/users/profile/${sectionType.toLowerCase()}/${id}`;
        }
        let options = {
            method: operation, // 'GET', 'POST', 'PUT', 'DELETE
            headers: {
                'Content-Type': 'application/json',
            },
        };

        if (operation !== 'GET') {
            console.log(sectionData)
            options.body = JSON.stringify(sectionData);
        }

        const response = await fetch(url, options);

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }
        if(operation == 'GET'){
            const json = await response.json();
            return json
        }
    } catch (error) {
        console.log(error)
        console.error(`Error in ${operation} ${sectionType}:`, error.message);
    }
}

async function getUserSkills(userName) {
    const url = `${backendURL}/users/${userName}/profile/skills`;
    try {
        const response = await fetch(url, {
            method: 'GET', // Specify the method here
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }
        const json = await response.json();
        return json;
  } catch (error) {
    console.error(error.message);
  }
}

async function fetchSkills(){
    console.log("Fetching skills...");
    const url = 'https://linkedin-data-api.p.rapidapi.com/get-article-comments?url=https%3A%2F%2Fwww.linkedin.com%2Fpulse%2F2024-corporate-climate-pivot-bill-gates-u89mc%2F%3FtrackingId%3DV85mkekwT9KruOXln2gzIg%253D%253D&page=1&sort=REVERSE_CHRONOLOGICAL';
    const options = {
        method: 'GET',
        headers: {
            'x-rapidapi-key': '84b64d8238msh373aa746ad6716cp1691d9jsn73cd9aabc50e',
            'x-rapidapi-host': 'linkedin-data-api.p.rapidapi.com'
        }
    };

    try {
        const response = await fetch(url, options);
        const result = await response.json();  // Parse as JSON
        console.log(result);
        return result; // Assuming the result is an array of skills
    } catch (error) {
        console.error("Error fetching skills:", error);
    }
}
async function editUserSkills(userName, skills){
    const url = `${backendURL}/users/${userName}/profile/skills`;
    try {
        const response = await fetch(url, {
            method: 'PUT', // Specify the method here
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(skills)
        });

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }
  } catch (error) {
    console.error(error.message);
  }
}
export{
    getUserProfile,
    editUserProfile,
    handleCustomSectionOperation,
    getUserSkills,
    fetchSkills,
    editUserSkills
}