import "./Navbar.css";
import NotificationBell from "../notificationbell/NotificationBell";
import { Link } from "react-router-dom";
import SignUp from "../../pages/signup/SignUp";

const Navbar = () => {
  return (
    <div className="navbar">
      <div>brand</div>
      <ul className="nav-links">
        <li className="nav-link"><Link to ="/">Home</Link></li>
        <li className="nav-link"><Link to="/interviews">Interviews</Link></li>
        <li className="nav-link"><Link to="/about">About</Link></li>
        <li className="nav-link"><Link to="/contact">Contact</Link></li>
        <li className="nav-link">
          <NotificationBell />
        </li>
    
      </ul>
      <div><Link to="/login">Login</Link></div>
    </div>
  );
};

export default Navbar;
