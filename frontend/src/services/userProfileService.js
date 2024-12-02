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
    userName,
    sectionData = null
) {
    try {
        const url = `${backendURL}/users/${userName}/profile/${sectionType.toLowerCase()}`;
        let options = {
            method: operation, // 'GET', 'POST', or 'PUT'
            headers: {
                'Content-Type': 'application/json',
            },
        };

        if (operation !== 'GET') {
            options.body = JSON.stringify(sectionData);
        }

        const response = await fetch(url, options);

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(`Error in ${operation} ${sectionType}:`, error.message);
    }
}


export{
    getUserProfile,
    editUserProfile,
    handleCustomSectionOperation
}