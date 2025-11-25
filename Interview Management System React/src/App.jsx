import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Home from './pages/home/Home.jsx'
import About from './pages/about/About.jsx'
import Contact from './pages/contact/Contact.jsx'
import Login from './pages/login/Login.jsx'
import Layout from './layout/Layout.jsx'
import Interviews from './pages/interviews/Interviews.jsx'
import EditInterview from './pages/edit-interview/EditInterview.jsx'

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
      </Routes>
    </Router>
  )
}

export default App