import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import SignUpPage from './pages/signUpPage/SignUpPage'
function App() {
  return (
    <>
  <Router>  
    <Routes>
      <Route path="/" element={<SignUpPage/>}/>
    </Routes> 
  </Router>
    </>
  )
}

export default App
