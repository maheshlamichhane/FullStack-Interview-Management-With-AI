import Header from '../components/header/Header'
import Navbar from '../components/navbar/Navbar'
import Footer from '../components/footer/Footer'
const Layout = ({ children }) => {
  return (
    <div>
      <Header />
      <Navbar />
      <main>
        {children}
      </main>
      <Footer />
    </div>
  )
}

export default Layout