const backendUrl = import.meta.env.VITE_BACKEND_URL;


export async function fetchJobs(filters, page = 1, offset = 10) {
    const username = document.cookie.split('; ')[1].split('=')[1];
    const url = `${backendUrl}/home/${username}/jobs/filter`; 
    

    const query = `query=${filters.searchQuery}`
    const level = `level=${filters.jobLevel}`
    const workLocation = `workLocation=${filters.workLocation}`
    const employmentType = `employmentType=${filters.employmentType}`
    const salary = `salary=${filters.salary}`
    const sortBy = `sort=${filters.sortBy}`   // what is its order in the query
    // console.log(`${url}?${query}&${level}&${workLocation}&${employmentType}&${salary}&${sortBy}&page=${page}&offset=${offset}`);
    const response = await fetch(`${url}?${query}&${level}&${workLocation}&${employmentType}&${salary}&${sortBy}&page=${page}&offset=${offset}`,
        {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        }
    );
    
    const data = await response.json();


    if (!response.ok)
        throw new Error('Failed to fetch jobs');
    
    console.log(data)
    return data; // Assuming the backend returns a JSON object with job data.
}

export async function toggleSaveJob(job, saved) {
    const username = document.cookie.split('; ')[1].split('=')[1];
    // console.log(username);  //username is correct using this method


    const url = `${backendUrl}/home/${username}/jobs/${job.id}`;

    if (saved) {

        const response = await fetch(url + '/unsave', {
            method: 'DELETE',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok)
            throw new Error('Failed to unsave job');
    }

    else {
        const response = await fetch(url + '/save', {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok)
            throw new Error('Failed to save job');
    }

}