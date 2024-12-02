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
        console.log(operation, id, url )
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
            console.log(json)
            return json
        }
    } catch (error) {
        console.log(error)
        console.error(`Error in ${operation} ${sectionType}:`, error.message);
    }
}

async function getAllSkills() {
    const url = `${backendURL}/skills}`;
    try {
        const response = await fetch(url, {
            method: 'GET', // Specify the method here
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
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
async function addSkill(userName, skillId) {
    const url = `${backendURL}/users/${userName}/profile/skill`;
    try {
        const response = await fetch(url, {
            method: 'POST', // Specify the method here
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: skillId
            })
        });

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }
  } catch (error) {
    console.error(error.message);
  }
}
async function removeSkill(userName, skillId) {
    const url = `${backendURL}/users/${userName}/profile/skill`;
    try {
        const response = await fetch(url, {
            method: 'DELETE', // Specify the method here
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                id: skillId
            })
        });

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }
        const json = await response.json();
  } catch (error) {
    console.error(error.message);
  }
}
export{
    getUserProfile,
    editUserProfile,
    handleCustomSectionOperation,
    getAllSkills,
    getUserSkills,
    addSkill,
    removeSkill
}