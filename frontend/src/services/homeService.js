


export async function fetchJobs(filters, page = 1, offset = 10) {
    const url = `http://localhost:8080/home/jobs/filter`; 
    const query = `query=${filters.searchQuery}`
    const level = `level=${filters.jobLevel}`
    const location = `location=${filters.location}`
    const employmentType = `type=${filters.employmentType}`
    const salary = `salary=${""}`
    const sortBy = `sortBy=${filters.sortBy}`   // what is its order in the query

    console.log(filters)

    const response = await fetch(`${url}?${query}&${level}&${location}&${employmentType}&${salary}&page=${page}&offset=${offset}`,
        {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        }
    );
    
    const data = await response.json();
    console.log(data);

    if (!response.ok)
        throw new Error('Failed to fetch jobs');
    
    return data; // Assuming the backend returns a JSON object with job data.
}
