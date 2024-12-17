


export async function fetchJobs(filters, page = 1, offset = 10) {
    const url = `http://localhost:8080/home/jobs/filter`; 
    const query = `query=${filters.searchQuery}`
    const level = `level=${filters.jobLevel}`
    const workLocation = `workLocation=${filters.workLocation}`
    const employmentType = `employmentType=${filters.employmentType}`
    const salary = `salary=${filters.salary}`
    const sortBy = `sort=${filters.sortBy}`   // what is its order in the query
    const response = await fetch(`${url}?${query}&${level}&${workLocation}&${employmentType}&${salary}&${sortBy}&page=${page}&offset=${offset}`,
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
