package cz.rozek.jan.base_auth_api_framework.services;

import cz.rozek.jan.base_auth_api_framework.permissions.Permission;
import cz.rozek.jan.base_auth_api_framework.services.auth.IAuthorizationService;

public interface ISecuredService {
    
    void setAuthorizationService(IAuthorizationService authorizationService);
    IAuthorizationService getAuthorizationService();
    
    void verifyAccess(String accessJWT, Permission requiredPermition);
    void verifyNullTokon(Permission requiredPermition);
}
