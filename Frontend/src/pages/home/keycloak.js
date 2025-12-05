// src/lib/keycloak.js
import Keycloak from 'keycloak-js';

// Initialize Keycloak with your realm and client details
const keycloak = new Keycloak({
    url: 'http://localhost:7080',        // Your Keycloak server URL
    realm: 'Interview',                  // Your realm name
    clientId: 'react-frontend'                // Your client ID (must be configured as a Public client)
});

// Configure initialization options
export const initOptions = {
    onLoad: 'check-sso',                 // Silent authentication check on app load
    flow: 'standard',                    // Uses the secure Authorization Code Flow with PKCE
    pkceMethod: 'S256',                  // Proof Key for Code Exchange for security
    checkLoginIframe: false,             // Disable iframe check for simplicity; enable for production
    silentCheckSsoRedirectUri: `${window.location.origin}/silent-check-sso.html`
};

export default keycloak;