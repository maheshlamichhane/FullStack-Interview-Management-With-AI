import { useEffect, useState } from "react";
import axios from "axios";

const BuildVersion = () => {
  const [version, setVersion] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchVersion = async () => {
      try {
        const response = await axios.get("http://localhost:8085/api/interviews");
        setVersion(response.data);
      } catch (err) {
        console.error("Error fetching version:", err);
        setError("Failed to fetch version");
      }
    };

    fetchVersion();
  }, []);

  return (
    <div style={{ padding: "20px" }}>
      <h1>Build Version</h1>
      {version && <p>Version: {version}</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
};

export default BuildVersion;
