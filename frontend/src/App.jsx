import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'

import UserHome from './pages/userHome/userHome'
import Profile from './pages/profilePage/profilePage'
function App() {
  return (
    <div className='app'>
  <Router>  
    <Routes>
      <Route path="applicant/:userName/profile/" element={<Profile/>} />
      <Route path="applicant/:userName/profile/:profileSection" element={<Profile/>} />
      <Route path="/home" element={<UserHome/>}></Route>
    </Routes> 
  </Router>
    </div>
  )
}

export default App
