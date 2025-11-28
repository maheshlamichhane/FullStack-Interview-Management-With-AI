// components/NotificationDropdown.js
import React, { useState, useEffect, useRef } from 'react';
import './NotificationDropdown.css';

const NotificationDropdown = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [notifications, setNotifications] = useState([]);
    const [unreadCount, setUnreadCount] = useState(0);
    const [loading, setLoading] = useState(false);
    const dropdownRef = useRef(null);

    // Close dropdown when clicking outside
    useEffect(() => {
        const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    // Load notifications from API
    useEffect(() => {
        fetchNotifications();
        fetchUnreadCount();
    }, []);

    // WebSocket connection for real-time updates
    useEffect(() => {
        const ws = new WebSocket('ws://localhost:8080/ws/interview');
        
        ws.onopen = () => {
            console.log('🔔 Notification WebSocket connected');
        };

        ws.onmessage = async (event) => {
            try {
                const message = JSON.parse(event.data);
                console.log('📨 New real-time notification:', message);
                
                await fetchNotifications();
                await fetchUnreadCount();
                
            } catch (error) {
                console.error('Error parsing notification:', error);
            }
        };

        return () => {
            ws.close();
        };
    }, []);

    const fetchNotifications = async () => {
        try {
            setLoading(true);
            const response = await fetch('http://localhost:8080/api/notifications');
            if (response.ok) {
                const data = await response.json();
                setNotifications(data);
            }
        } catch (error) {
            console.error('Error fetching notifications:', error);
        } finally {
            setLoading(false);
        }
    };

    const fetchUnreadCount = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/notifications/unread-count');
            if (response.ok) {
                const data = await response.json();
                setUnreadCount(data.count);
            }
        } catch (error) {
            console.error('Error fetching unread count:', error);
        }
    };

    const toggleDropdown = async () => {
        const newIsOpen = !isOpen;
        setIsOpen(newIsOpen);
        
        if (newIsOpen && unreadCount > 0) {
            await markAllAsRead();
        }
    };

    const markAllAsRead = async () => {
        try {
            await fetch('http://localhost:8080/api/notifications/mark-all-read', {
                method: 'POST',
            });
            await fetchUnreadCount();
            await fetchNotifications();
        } catch (error) {
            console.error('Error marking all as read:', error);
        }
    };

    const clearAllNotifications = async () => {
        try {
            await fetch('http://localhost:8080/api/notifications', {
                method: 'DELETE',
            });
            setNotifications([]);
            setUnreadCount(0);
        } catch (error) {
            console.error('Error clearing notifications:', error);
        }
    };

    const formatTime = (timestamp) => {
        if (!timestamp) return '';
        
        const date = new Date(timestamp);
        const now = new Date();
        const diffInMinutes = (now - date) / (1000 * 60);
        const diffInHours = diffInMinutes / 60;
        
        if (diffInMinutes < 1) {
            return 'Just now';
        } else if (diffInMinutes < 60) {
            return `${Math.floor(diffInMinutes)}m ago`;
        } else if (diffInHours < 24) {
            return `${Math.floor(diffInHours)}h ago`;
        } else {
            return date.toLocaleDateString();
        }
    };

    return (
        <div className="notification-dropdown" ref={dropdownRef}>
            {/* Notification Bell Button */}
            <button 
                className="notification-trigger"
                onClick={toggleDropdown}
                aria-label="Notifications"
            >
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                    <path 
                        d="M18 8C18 6.4087 17.3679 4.88258 16.2426 3.75736C15.1174 2.63214 13.5913 2 12 2C10.4087 2 8.88258 2.63214 7.75736 3.75736C6.63214 4.88258 6 6.4087 6 8C6 15 3 17 3 17H21C21 17 18 15 18 8Z" 
                        strokeWidth="2" 
                        strokeLinecap="round" 
                        strokeLinejoin="round"
                    />
                    <path 
                        d="M13.73 21C13.5542 21.3031 13.3019 21.5547 12.9982 21.7295C12.6946 21.9044 12.3504 21.9965 12 21.9965C11.6496 21.9965 11.3054 21.9044 11.0018 21.7295C10.6982 21.5547 10.4458 21.3031 10.27 21" 
                        strokeWidth="2" 
                        strokeLinecap="round" 
                        strokeLinejoin="round"
                    />
                </svg>
                {unreadCount > 0 && (
                    <span className="notification-badge">{unreadCount > 99 ? '99+' : unreadCount}</span>
                )}
            </button>

            {/* Dropdown Content - Appears above everything */}
            {isOpen && (
                <div className="notification-dropdown-content">
                    <div className="notification-header">
                        <h3>Notifications</h3>
                        <div className="notification-actions">
                            <button 
                                className="action-btn" 
                                onClick={markAllAsRead}
                                disabled={unreadCount === 0}
                            >
                                Mark all read
                            </button>
                            <button 
                                className="action-btn" 
                                onClick={clearAllNotifications}
                                disabled={notifications.length === 0}
                            >
                                Clear all
                            </button>
                        </div>
                    </div>

                    <div className="notification-list">
                        {loading ? (
                            <div className="notification-loading">Loading notifications...</div>
                        ) : notifications.length === 0 ? (
                            <div className="notification-empty">
                                <div className="empty-icon">🔔</div>
                                <p>No notifications yet</p>
                                <small>Notifications will appear here</small>
                            </div>
                        ) : (
                            notifications.map(notification => (
                                <div 
                                    key={notification.id} 
                                    className={`notification-item ${notification.read ? 'read' : 'unread'}`}
                                >
                                    <div className="notification-type-icon">
                                        {getNotificationIcon(notification.type)}
                                    </div>
                                    <div className="notification-content">
                                        <div className="notification-title">
                                            {notification.title}
                                        </div>
                                        <div className="notification-message">
                                            {notification.message}
                                        </div>
                                        <div className="notification-time">
                                            {formatTime(notification.createdAt)}
                                        </div>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};

const getNotificationIcon = (type) => {
    switch (type) {
        case 'interview_created':
            return '📅';
        case 'interview_updated':
            return '✏️';
        case 'participant_joined':
            return '👤';
        case 'status_change':
            return '🔄';
        case 'participant_action':
            return '👥';
        default:
            return '🔔';
    }
};

export default NotificationDropdown;