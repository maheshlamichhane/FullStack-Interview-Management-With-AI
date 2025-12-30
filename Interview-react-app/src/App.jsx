import { useEffect, useState, useContext } from "react";
import { AuthContext } from "react-oauth2-code-pkce";

function App(){
  // const { token } = useContext(AuthContext);
  // const [version, setVersion] = useState("");
  // const [error, setError] = useState("");

  const [version, setVersion] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");



const fetchBuildVersion = async () => {
    setLoading(true);
    setError("");
    setVersion("");

    try {
      const response = await fetch("http://localhost:8080/build-version", {
        method: "GET"
      });

      if (!response.ok) {
        throw new Error("Failed to fetch build version");
      }

      const data = await response.text(); // "3.0"
      setVersion(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };


  // useEffect(() => {
  //   if (!token) return;

  //   const fetchVersion = async () => {
  //     try {
  //       const response = await fetch("http://localhost:8085/api/interviews/build-version", {
  //         headers: {
  //           "Authorization": `Bearer ${token}`,
  //           "Content-Type": "text/plain; charset=utf-8"
  //         }
  //       });

  //       if (response.ok) {
  //         const text = await response.text();
  //         setVersion(text);
  //       } else {
  //         setError(`Error: ${response.status} ${response.statusText}`);
  //       }
  //     } catch (err) {
  //       console.error("Error fetching build version:", err);
  //       setError("Failed to fetch build version");
  //     }
  //   };

  //   fetchVersion();
  // }, [token]);

  return (
    <div>
      <h1>Welcome to the Interview App</h1>
  <button onClick={fetchBuildVersion}>
        Get Build Version
      </button>
      {loading && <p>Loading...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
      {version && <p>Build Version: {version}</p>}
    </div>
  );
    // <div style={{ marginTop: "2rem" }}>
    //   <h2>Build Version</h2>
    //   {version && <p>{version}</p>}
    //   {error && <p style={{ color: "red" }}>{error}</p>}
    // </div>
  // );
};

export default App;
