import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import SignUpPage from './pages/signUpPage/SignUpPage'
import LogInPage from './pages/logInPage/LogInPage'
function App() {
  return (
    <>
  <Router>  
    <Routes>
      <Route path="/" element={<SignUpPage/>}/>
      <Route path="/login" element={<LogInPage/>}/>
    </Routes> 
  </Router>
    </>
  )
}

export default App
