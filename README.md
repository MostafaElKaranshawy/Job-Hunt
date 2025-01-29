# Job Hunt

Online Jobs Platform to help job seekers find their preferred jobs.

## Table of Contents

- [Job Hunt](#job-hunt)
  - [Table of Contents](#table-of-contents)
    - [Features](#features)
    - [Technologies](#technologies)
    - [Design-Patterns](#design-patterns)
    - [Installation](#installation)
  - [Contributors](#contributors)

### Features

- **Authentication**:

  - Signup and Login for Applicant User
    - with Email and Password
    - with Google
  - Signup and Login for Company User
    - with Email and Password
  - Forget and Reset Password

- **Applicant Profile**
  - Personal Profile
    - Personal Information
    - Previous Education
    - Previous Experience
    - Skills
  - Saved Jobs
  - Applied Jobs Applications
  - User Settings
    - Change account password

- **Company Profile**
  - Company Information
    - name
    - address
    - phone number
    - specialization
    - Change Password
  - Jobs Cards
    - Create New Jobs Opportunities
    - See Previous Jobs
      - See Jobs Applicants Applications.
        - Accept or Reject Applicants.
        - Report Applicants.
    - Delete Jobs.
- **Applicants Home**
  - Applicant can filter jobs for a certain criteria
    - Job Type
    - Job Category
    - Job Level
    - Job Location
    - Job Salary
  - Search for certain Job Title or Company Name.
  - Sort Jobs according to their Salary or posted time.
  - Save, Unsave or Apply for a job.
- **Job Applications**
  - Fill the Job Form to apply for it.
  - Report the Job for any reason.
- **Admin Profile**
  - Admin can see the reported Users and jobs and their reasons
    - Admin can neglect the report.
    - Admin can ban repotred users.
    - Admin can remove reported jobs.

### Technologies

- **Backend**
  - Development
    - Spring Boot
    - Java
  - Database Development
    - SQL
    - JPA
  - Authentication and Authentication:
    - JWT Authentication
    - Google OAuth Authentication
    - Cookies

- **Frontend**
  - Development
    - React js
    - JSON
    - CSS
  - Routing
  - Cookies
  - DOM Manipulation

- **Process Management**
  - Software Process Model
    - Agile
  - Problem Tracking
    - Jira
  - Version Control
    - Git & Github

### Design-Patterns

- **Factory** to create different user types (Applicant, Company)
- **Chain of Responsibility** to manage the validation process
- **Filter** to apply different criteria for filtering jobs.
- **Strategy** to apply different sorting criteria for sorting jobs.

### Installation

To use the tools and examples provided in this repository, you need to have npm installed on your system. You can install the necessary dependencies by cloning the repository and running the installation command.

1. Clone the repository to your local machine.
2. Navigate to the repository directory.
3. Install the required dependencies.


## Contributors

- [Ahmed Emara](https://github.com/Emara25)
- [Mustafa ElKaranshawy](https://github.com/MostafaElKaranshawy)
- [Mustafa Kamel](https://github.com/mustafakamel7)
- [Omnia Karem](https://github.com/OmniaKarem)
- [Youssef Mahmoud](https://github.com/Youssef-Mahmoud0)
- [Youmna Yasser](https://github.com/yomnay888)
