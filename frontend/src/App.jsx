import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ProfilePage from './pages/profilePage/ProfilePage';
import Jobs from './pages/jobsPage/Jobs';

const App = () => {
  return (
    <div className="app">
    <Router>
        <Routes>
          <Route path="/" element={<ProfilePage/>} />
          <Route path="/jobs" element={<Jobs/>} />
        </Routes>
    </Router>
    </div>
  );
};

export default App;
