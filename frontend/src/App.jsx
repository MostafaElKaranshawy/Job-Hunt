import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import CompanyProfilePage from './pages/profilePage/CompanyProfilePage';
import Jobs from './pages/jobsPage/Jobs';

const App = () => {
  return (
    <div className="app">
    <Router>
        <Routes>
          <Route path="/company/:companyUsername" element={<CompanyProfilePage/>} />
          <Route path="/company/:companyUsername/jobs" element={<Jobs/>} />
        </Routes>
    </Router>
    </div>
  );
};

export default App;
