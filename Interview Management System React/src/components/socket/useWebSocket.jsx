import { useState, useEffect, useRef, useCallback } from 'react';

const useWebSocket = (url) => {
    const [isConnected, setIsConnected] = useState(false);
    const [messages, setMessages] = useState([]);
    const [lastMessage, setLastMessage] = useState(null);
    const [pendingRequests, setPendingRequests] = useState(new Map());
    const ws = useRef(null);
    const requestIdCounter = useRef(0);

    // Generate unique request ID
    const generateRequestId = useCallback(() => {
        return `req_${Date.now()}_${requestIdCounter.current++}`;
    }, []);

    useEffect(() => {
        const connectWebSocket = () => {
            try {
                ws.current = new WebSocket(url);

                ws.current.onopen = () => {
                    console.log('✅ WebSocket connected');
                    setIsConnected(true);
                };

                ws.current.onclose = (event) => {
                    console.log('❌ WebSocket disconnected', event.code, event.reason);
                    setIsConnected(false);
                    
                    // Attempt reconnection after 3 seconds
                    setTimeout(() => {
                        console.log('🔄 Attempting to reconnect...');
                        connectWebSocket();
                    }, 3000);
                };

                ws.current.onmessage = (event) => {
                    try {
                        let message;
                        
                        // Handle both JSON and plain text messages
                        if (typeof event.data === 'string') {
                            try {
                                message = JSON.parse(event.data);
                            } catch {
                                // If it's not JSON, treat as plain text
                                message = {
                                    type: 'RAW_MESSAGE',
                                    content: event.data,
                                    timestamp: Date.now()
                                };
                            }
                        } else {
                            message = {
                                type: 'BINARY_MESSAGE',
                                data: event.data,
                                timestamp: Date.now()
                            };
                        }

                        console.log('📨 Received WebSocket message:', message);
                        
                        // Update state
                        setLastMessage(message);
                        setMessages(prev => [...prev.slice(-99), message]); // Keep last 100 messages

                        // Handle request-response pattern
                        if (message.requestId && pendingRequests.has(message.requestId)) {
                            const { resolve, reject } = pendingRequests.get(message.requestId);
                            pendingRequests.delete(message.requestId);
                            
                            if (message.type === 'ERROR') {
                                reject(new Error(message.error || 'Request failed'));
                            } else {
                                resolve(message);
                            }
                        }

                        // Handle broadcast messages (no requestId)
                        if (!message.requestId) {
                            handleBroadcastMessage(message);
                        }

                    } catch (error) {
                        console.error('❌ Error processing WebSocket message:', error);
                    }
                };

                ws.current.onerror = (error) => {
                    console.error('❌ WebSocket error:', error);
                    setIsConnected(false);
                };

            } catch (error) {
                console.error('❌ Failed to create WebSocket connection:', error);
            }
        };

        connectWebSocket();

        // Cleanup on unmount
        return () => {
            if (ws.current) {
                ws.current.close(1000, 'Component unmounted');
            }
        };
    }, [url, pendingRequests]);

    // Handle different types of broadcast messages
    const handleBroadcastMessage = useCallback((message) => {
        switch (message.type) {
            case 'INTERVIEW_CREATED':
                console.log('📢 Interview created notification:', message);
                // You can emit events or call callbacks here
                break;
            case 'INTERVIEW_UPDATED':
                console.log('📢 Interview updated notification:', message);
                break;
            case 'USER_NOTIFICATION':
                console.log('📢 User notification:', message);
                break;
            case 'SYSTEM_MESSAGE':
                console.log('📢 System message:', message);
                break;
            default:
                console.log('📢 Unknown broadcast message type:', message.type);
        }
    }, []);

    // Send message and wait for response (request-response pattern)
    const sendMessageWithResponse = useCallback(async (message, timeout = 5000) => {
        return new Promise((resolve, reject) => {
            if (!ws.current || ws.current.readyState !== WebSocket.OPEN) {
                reject(new Error('WebSocket is not connected'));
                return;
            }

            const requestId = generateRequestId();
            const messageWithId = {
                ...message,
                requestId,
                timestamp: Date.now()
            };

            // Set timeout for response
            const timeoutId = setTimeout(() => {
                if (pendingRequests.has(requestId)) {
                    pendingRequests.delete(requestId);
                    reject(new Error('Request timeout'));
                }
            }, timeout);

            // Store the promise callbacks
            pendingRequests.set(requestId, {
                resolve: (response) => {
                    clearTimeout(timeoutId);
                    resolve(response);
                },
                reject: (error) => {
                    clearTimeout(timeoutId);
                    reject(error);
                }
            });

            try {
                const messageStr = JSON.stringify(messageWithId);
                ws.current.send(messageStr);
                console.log('📤 Sent WebSocket message with ID:', requestId, message);
            } catch (error) {
                pendingRequests.delete(requestId);
                clearTimeout(timeoutId);
                reject(error);
            }
        });
    }, [isConnected, generateRequestId, pendingRequests]);

    // Simple send message (fire and forget)
    const sendMessage = useCallback((message) => {
        if (!ws.current || ws.current.readyState !== WebSocket.OPEN) {
            console.error('❌ Cannot send message: WebSocket not connected');
            return false;
        }

        try {
            const messageToSend = {
                ...message,
                timestamp: Date.now()
            };
            
            const messageStr = JSON.stringify(messageToSend);
            ws.current.send(messageStr);
            console.log('📤 Sent WebSocket message:', message);
            return true;
        } catch (error) {
            console.error('❌ Error sending message:', error);
            return false;
        }
    }, [isConnected]);

    // Close connection manually
    const disconnect = useCallback(() => {
        if (ws.current) {
            ws.current.close(1000, 'Manual disconnect');
        }
    }, []);

    return {
        isConnected,
        messages,
        lastMessage,
        sendMessage,
        sendMessageWithResponse,
        disconnect,
        pendingRequests: pendingRequests.size
    };
};

export default useWebSocket;