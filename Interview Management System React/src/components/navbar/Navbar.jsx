import "./Navbar.css"
import { useEffect, useState } from "react"
import NotificationDropdown from "../notification dropdown/NotificationDropdown"
import NotificationBell from "../notificationbell/NotificationBell";
import useWebSocket from "../socket/useWebSocket";
const Navbar = () => {

  const { 
          isConnected, 
          lastMessage
      } = useWebSocket('ws://localhost:8080/ws/interview');
       const [notificationCount, setNotificationCount] = useState(0);


 // Handle incoming messages
    useEffect(() => {
       setNotificationCount(prev => prev + 1);
        if (lastMessage) {
            switch (lastMessage.type) {
                case 'INTERVIEW_CREATED_RESPONSE':
                    console.log('✅ Server confirmed interview creation:', lastMessage);
                    alert(`Interview "${lastMessage.interview?.title}" created successfully!`);
                    break;
                    
                case 'INTERVIEW_CREATED_BROADCAST':
                    console.log('📢 Interview created broadcast:', lastMessage);
                    // Update UI or show notification to other users
                    break;
                    
                case 'ERROR':
                    console.error('❌ Server error:', lastMessage);
                    alert(`Error: ${lastMessage.message}`);
                    break;
                    
                default:
                    console.log('📨 Received message:', lastMessage);
            }
        }
    }, [lastMessage]);


  return (
    <div className="navbar">
        <div>brand</div>
        <ul className="nav-links">
            <li className="nav-link"><a href="/">Home</a></li>
            <li className="nav-link"><a href="/interviews">Interviews</a></li>
            <li className="nav-link"><a href="/about">About</a></li>
            <li className="nav-link"><a href="/contact">Contact</a></li>
            <li className="nav-link"><NotificationBell notificationCount = {notificationCount}/></li>
        </ul>
        <div>
          <a href="/login"> Login</a>
        </div>
              <div className="websocket-status">
          WebSocket: {isConnected ? '🟢 Connected' : '🔴 Disconnected'}
        </div>
    </div>
  
  )
}

export default Navbar