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

async function fetchSkills(query){
    let myHeaders = new Headers();
    myHeaders.append("apikey", "AfAbEfRxDYiRaNIzQHh54FUF2wSn2unP");

    let requestOptions = {
    method: 'GET',
    redirect: 'follow',
    headers: myHeaders
    };
    try {
        const response = await fetch(`https://api.apilayer.com/skills?q=${query}`, requestOptions);
        if (!response.ok) {
            throw new Error(`Error: ${response.statusText}`);
        }
        const result = await response.json(); // Parse the JSON response
        return result; // Ensure the result is returned
    } catch (error) {
        console.error('Error fetching skills:', error);
        return []; // Return an empty array or appropriate default on error
    }
}
async function editUserSkills(userName, skills){
    console.log(userName, skills)
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