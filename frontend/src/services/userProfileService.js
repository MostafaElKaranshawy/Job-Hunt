const backendURL = 'http://localhost:8080';

async function getUserProfile(userName) {
    try{
        const url = `${backendURL}/users/${userName}/profile`;
        const response = await fetch(url, {
            method: 'GET', // Specify the method here
            credentials: 'include',
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
            credentials : 'include',
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
            credentials: 'include',
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
            credentials: 'include',
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

async function editUserSkills(userName, skills){
    console.log(userName, skills)
    const url = `${backendURL}/users/${userName}/profile/skills`;
    try {
        const response = await fetch(url, {
            method: 'PUT', // Specify the method here
            credentials: 'include',
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

async function changePassword(userName, passwordData) {
    console.log("passwordData", passwordData);
    const url = `${backendURL}/user/${userName}/profile/password`;
    try {
        const response = await fetch(url, {
            method: 'PUT', // Specify the method here
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(passwordData)
        });
        console.log(response)
        if (!response.ok) {
            console.log(response)
            throw new Error(`Response status: ${response.status}`);
        }
        return "ok";
  } catch (error) {
    return error.message;
  }

}

async function getSavedJobs(username, page, offset) {
    const url = `${backendURL}/users/${username}/profile/savedJobs?page=${page}&offset=${offset}`;
    try {
        const response = await fetch(url, {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }
        const json = await response.json();

        console.log(json);
        return json;
  } catch (error) {
    console.error(error.message);
  }
}
export{
    getUserProfile,
    editUserProfile,
    handleCustomSectionOperation,
    getUserSkills,
    editUserSkills,
    changePassword,
    getSavedJobs
}