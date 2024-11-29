import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'

import UserHome from './pages/userHome/userHome'

function App() {
  return (
    <>
  <Router>  
    <Routes>
      {/* <Route path="/" element={<SignUpPage/>}/> */}
      <Route path="/home" element={<UserHome/>}></Route>
    </Routes> 
  </Router>
    </>
  )
}

export default App
