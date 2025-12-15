export const authConfig = {
    clientId: 'react-frontend',
    authorizationEndpoint: 'http://localhost:7080/realms/Interview/protocol/openid-connect/auth',
    tokenEndpoint: 'http://localhost:7080/realms/Interview/protocol/openid-connect/token',
    redirectUri: 'http://localhost:5173',
    scope: 'openid profile email offline_access',
    onRefreshTokenExpire: (event) => event.logIn(),
};