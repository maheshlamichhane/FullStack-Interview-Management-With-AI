import useWebSocket from "../socket/useWebSocket";
import { useEffect, useState } from "react";
const NotificationBell = (props) => {

    const [notificationCount, setNotificationCount] = useState(0);
    const { isConnected, lastMessage } = useWebSocket(
    "ws://localhost:8080/ws/interview"
  );

    useEffect(() => {

      console.log("NotificationBell - New WebSocket message:", lastMessage);
      console.log("NotificationBell - Current notification count:", notificationCount);
      console.log("NotificationBell - WebSocket connected:", isConnected);

    if (!lastMessage) return;
    const notifyTypes = [
      "interview_update",
      "INTERVIEW_CREATED_BROADCAST",
      "INTERVIEW_UPDATED",
      "USER_NOTIFICATION",
    ];

    if (notifyTypes.includes(lastMessage.type)) {
      console.log("Here");
      setNotificationCount((prev) => prev + 1);
    }

    switch (lastMessage.type) {
      case "interview_update":
        alert(`Interview "${lastMessage.interview?.title}" created successfully!`);
        break;

      case "ERROR":
        alert(`Error: ${lastMessage.message}`);
        break;

      default:
        console.log("📩 Other message:", lastMessage);
    }
  }, [lastMessage]);

  console.log("Notification count=", notificationCount);

  return (
    <div className="notification_container">
            <div className="notification_bell">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor">
                <path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.64-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z"/>
              </svg>
              
              {notificationCount > 0 && (
                <span className="notification_badge">
                  {notificationCount > 99 ? '99+' : notificationCount}
                </span>
              )}
            </div>
    </div>
  )
}

export default NotificationBell