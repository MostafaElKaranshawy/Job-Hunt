const backendURL = 'http://localhost:8080';

async function fetchSkills(query) {
    let myHeaders = new Headers();
    myHeaders.append("apikey", "AfAbEfRxDYiRaNIzQHh54FUF2wSn2unP");

    let requestOptions = {
        method: 'GET',
        redirect: 'follow',
        headers: myHeaders,
        Credentials: 'include'
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

async function fetchCountries() {
    fetch("./json/countries.json")
        .then(response => {
            console.log(response);
            // return response.json(); // Ensure you return the parsed JSON here
        })
        // .then(data => {
        //     console.log(data); // Now you can use the data
        //     return data; // If you need to return the data for further use
        // })
        .catch(error => {
            console.error('Error fetching countries:', error);
            return []; // Return an empty array as a fallback
        });

}

export {
    fetchSkills,
    fetchCountries
}