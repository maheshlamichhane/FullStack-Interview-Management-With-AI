import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import keycloak,{initOptions} from './pages/home/keycloak.js';
import { ReactKeycloakProvider } from '@react-keycloak/web';
createRoot(document.getElementById('root')).render(
  <StrictMode>
       <ReactKeycloakProvider
      authClient={keycloak}
      initOptions={initOptions}
    >
 <App />
    </ReactKeycloakProvider>
   
    
  </StrictMode>,
)
