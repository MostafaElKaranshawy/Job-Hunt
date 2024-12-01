const backendURL = import.meta.env.VITE_BACKEND_URL;

async function getUserProfile(userID) {
    const url = `${backendURL}/users/${userID}/profile`;
    try {
        const response = await fetch(url,
            'GET',
            {
                headers: {
                    'Content-Type': 'application/json',
                }
            }
        );
        if (!response.ok) {
        throw new Error(`Response status: ${response.status}`);
        }

        const json = await response.json();
        console.log(json);
    } catch (error) {
        console.error(error.message);
    }
}

async function editUserProfile(userID, userData) {
    const url = `${backendURL}/users/${userID}/profile`;
  try {
    const response = await fetch(url,
        'PUT',
        {
            headers: {
            'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData),
        }
    );
    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }

    const json = await response.json();
    console.log(json);
  } catch (error) {
    console.error(error.message);
  }
}

