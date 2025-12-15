import { AuthContext } from 'react-oauth2-code-pkce'
import './App.css'
import { useContext, useEffect, useState } from 'react'

function App() {

  const { token, tokenData, logIn, logOut, isAuthenticated } = useContext(AuthContext);
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (token) {
      console.log("Token Available");
      fetchHello();
    }
  }, [token]);

  const fetchHello = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/home', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'text/plain; charset=utf-8'
        }
      });

      if (response.ok) {
        const text = await response.text();
        setMessage(text);
      }
    } catch (error) {
      console.warn('API ERROR: ', error);
      setMessage("API ERROR");
    }
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h1>OAUTH2 PKCE Demo</h1>
      <div>
        {!token ? (
          <button onClick={() => logIn()}>Login</button>
        ) : (
          <div>
            <button onClick={() => logOut()}>Logout</button>
            <h2>Message From API</h2>
            <p>{message}</p>

            <h2>Token Data</h2>
            <pre>{JSON.stringify(tokenData, null, 2)}</pre>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
