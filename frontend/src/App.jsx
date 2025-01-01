import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import UserHome from './pages/userHome/userHome'
import Profile from './pages/profilePage/profilePage'
import CompanyProfilePage from './pages/profilePage/CompanyProfilePage'
import Jobs from './pages/jobsPage/Jobs'
import SignUpPage from './pages/signupPage/SignUpPage'
import LogInPage from './pages/logInPage/LogInPage'
import ResetPasswordRequestPage from './pages/resetPasswordRequestPage/ResetPasswordRequestPage'
import ResetPasswordPage from './pages/resetPasswordPage/ResetPasswordPage'
import JobApplications from './pages/jobApplicationsPage/JobApplicationsPage'
import AdminDashboard from './pages/adminProfile/adminDashboard'
import AdminLogin from './pages/adminLoginPage/AdminLoginPage'

function App() {
    return (
        <div className='app'>
            <Router>
                <Routes>
                    <Route path="/" element={<SignUpPage/>}/>
                    <Route path="/login" element={<LogInPage/>}/>
                    <Route path="/company/:companyUsername" element={<CompanyProfilePage/>} />
                    <Route path="/company/:companyUsername/jobs" element={<Jobs/>} />
                    <Route path="user/:userName/profile/" element={<Profile/>} />
                    <Route path="user/:userName/profile/:profileSection" element={<Profile/>} />
                    <Route path="/home" element={<UserHome/>}></Route>
                    <Route path="/reset-password" element={< ResetPasswordPage/>} />
                    <Route path="/reset-password-request" element={< ResetPasswordRequestPage/>} />
                    <Route path="/jobs/:jobId/applications" element={<JobApplications/>}/>
                    <Route path="/admin/login" element={<AdminLogin/>}/>
                    <Route path="/admin/dashboard" element={<AdminDashboard/>} />
                </Routes>
            </Router>
        </div>
    )
}

export default App;
