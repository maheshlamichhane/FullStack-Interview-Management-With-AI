import { useState, useEffect, useRef, useCallback } from "react";

const useWebSocket = (url) => {
    const [isConnected, setIsConnected] = useState(false);
    const [messages, setMessages] = useState([]);
    const [lastMessage, setLastMessage] = useState(null);

    const ws = useRef(null);
    const pendingRequests = useRef(new Map());
    const requestIdCounter = useRef(0);
    const reconnectTimer = useRef(null);

    // Generate unique request ID
    const generateRequestId = () => {
        return `req_${Date.now()}_${requestIdCounter.current++}`;
    };

    const connectWebSocket = useCallback(() => {
        console.log("🔌 Creating new WebSocket...");

        ws.current = new WebSocket(url);

        ws.current.onopen = () => {
            console.log("🟢 WebSocket connected");
            setIsConnected(true);
        };

        ws.current.onclose = (event) => {
            console.log("🔴 WebSocket disconnected", event.code, event.reason);
            setIsConnected(false);

            // Try reconnecting after 3 seconds
            reconnectTimer.current = setTimeout(() => {
                console.log("🔄 Reconnecting WebSocket...");
                connectWebSocket();
            }, 3000);
        };

        ws.current.onerror = (error) => {
            console.error("❌ WebSocket error:", error);
            setIsConnected(false);
        };

        ws.current.onmessage = (event) => {
            let message;

            try {
                message = JSON.parse(event.data);
            } catch {
                message = { type: "RAW", content: event.data };
            }

            console.log("📨 Received:", message);

            setLastMessage(message);
            setMessages((prev) => [...prev.slice(-99), message]); // Keep last 100

            // Request-response handling
            if (message.requestId && pendingRequests.current.has(message.requestId)) {
                const { resolve, reject } = pendingRequests.current.get(message.requestId);
                pendingRequests.current.delete(message.requestId);

                if (message.type === "ERROR") reject(message);
                else resolve(message);
                return;
            }
        };
    }, [url]);

    // INITIALIZE SOCKET ONCE
    useEffect(() => {
        connectWebSocket();

        return () => {
            console.log("🧹 Cleaning up WebSocket...");
            if (reconnectTimer.current) clearTimeout(reconnectTimer.current);
            if (ws.current) ws.current.close(1000, "Component unmounted");
        };
    }, [connectWebSocket]);

    // Send message with response
    const sendMessageWithResponse = async (message, timeout = 5000) => {
        return new Promise((resolve, reject) => {
            if (!ws.current || ws.current.readyState !== WebSocket.OPEN) {
                reject(new Error("WebSocket not connected"));
                return;
            }

            const requestId = generateRequestId();
            const messageWithId = { ...message, requestId };

            pendingRequests.current.set(requestId, { resolve, reject });

            const timeoutId = setTimeout(() => {
                pendingRequests.current.delete(requestId);
                reject(new Error("Request timeout"));
            }, timeout);

            ws.current.send(JSON.stringify(messageWithId));
            console.log("📤 Sent:", messageWithId);

            // Cleanup on resolve/reject
            const originalResolve = resolve;
            resolve = (data) => {
                clearTimeout(timeoutId);
                originalResolve(data);
            };
        });
    };

    // Fire-and-forget send
    const sendMessage = (message) => {
        if (!ws.current || ws.current.readyState !== WebSocket.OPEN) {
            console.error("❌ Cannot send: WebSocket not connected");
            return false;
        }

        ws.current.send(JSON.stringify({ ...message, timestamp: Date.now() }));
        console.log("📤 Sent:", message);
        return true;
    };

    const disconnect = () => {
        if (ws.current) {
            ws.current.close(1000, "Manual disconnect");
        }
    };

    return {
        isConnected,
        messages,
        lastMessage,
        sendMessage,
        sendMessageWithResponse,
        disconnect,
        pendingRequestsCount: pendingRequests.current.size,
    };
};

export default useWebSocket;
