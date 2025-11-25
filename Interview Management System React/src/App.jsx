import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Home from './pages/home/Home.jsx'
import About from './pages/about/About.jsx'
import Contact from './pages/contact/Contact.jsx'
import Login from './pages/login/Login.jsx'
import Layout from './layout/Layout.jsx'
import Interviews from './pages/interviews/Interviews.jsx'
import EditInterview from './pages/edit-interview/EditInterview.jsx'
import SignUp from './pages/signup/SignUp.jsx'
import ForgotPassword from './pages/forgot password/ForgotPassword.jsx'
import ResetPassword from './pages/reset password/ResetPassword.jsx'
import OTPVerification from './pages/otp-verification/OtpVerification.jsx'


const App = () => {
  return (
    <Router>
      <Routes>
            <Route path="/" element={<Layout><Home /></Layout>} />
            <Route path="/interviews" element={<Layout><Interviews /></Layout>} />
            <Route path ="/interviews/:id/edit" element={<Layout><EditInterview/></Layout>} />
            <Route path="/about" element={<Layout><About /></Layout>} />
            <Route path="/contact" element={<Layout><Contact /></Layout> } />
            <Route path="/login" element={<Layout><Login /></Layout>} />
            <Route path = "/signup" element={<Layout><SignUp/></Layout>} />
            <Route path = "/forgot" element={<Layout><ForgotPassword/></Layout>} />
            <Route path="/reset-password" element={<Layout><ResetPassword/></Layout>} />
            <Route path ="/otp-verification" element={<Layout><OTPVerification/></Layout>}/>
      </Routes>
    </Router>
  )
}
export default App