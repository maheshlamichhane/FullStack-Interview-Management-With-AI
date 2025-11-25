import "./Navbar.css";
import NotificationBell from "../notificationbell/NotificationBell";


const Navbar = () => {
  return (
    <div className="navbar">
      <div>brand</div>
      <ul className="nav-links">
        <li className="nav-link"><a href="/">Home</a></li>
        <li className="nav-link"><a href="/interviews">Interviews</a></li>
        <li className="nav-link"><a href="/about">About</a></li>
        <li className="nav-link"><a href="/contact">Contact</a></li>
        <li className="nav-link">
          <NotificationBell/>
        </li>
      </ul>

      <div><a href="/login">Login</a></div>
    </div>
  );
};

export default Navbar;
