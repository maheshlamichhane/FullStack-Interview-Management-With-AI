export const authConfig = {
    clientId: 'react-frontend',
    authorizationEndpoint: 'http://localhost:8084/realms/Interview/protocol/openid-connect/auth',
    tokenEndpoint: 'http://localhost:8084/realms/Interview/protocol/openid-connect/token',
    redirectUri: 'http://localhost:3000/callback',
    scope: 'openid profile email offline_access',
    onRefreshTokenExpire: (event) => event.logIn(),
};