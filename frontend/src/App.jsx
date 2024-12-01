import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import CompanyProfilePage from './pages/profilePage/CompanyProfilePage';
import Jobs from './pages/jobsPage/Jobs';

const App = () => {
  return (
    <div className="app">
    <Router>
        <Routes>
          <Route path="/" element={<CompanyProfilePage/>} />
          <Route path="/jobs" element={<Jobs/>} />
        </Routes>
    </Router>
    </div>
  );
};

export default App;
