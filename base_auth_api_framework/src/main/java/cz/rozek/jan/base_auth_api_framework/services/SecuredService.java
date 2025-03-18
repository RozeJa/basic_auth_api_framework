package cz.rozek.jan.base_auth_api_framework.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.rozek.jan.base_auth_api_framework.exceptions.MissedAuthExeption;
import cz.rozek.jan.base_auth_api_framework.exceptions.SecurityException;
import cz.rozek.jan.base_auth_api_framework.permissions.Permission;
import cz.rozek.jan.base_auth_api_framework.services.auth.IAuthorizationService;

@Service
public class SecuredService implements ISecuredService {
    
    protected IAuthorizationService authorizationService;

    @Autowired
    public void setAuthorizationService(IAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }
    public IAuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    @Override
    public void verifyAccess(String accessJWT, Permission requiredPermition) {
        if (accessJWT == null)
            verifyNullTokon(requiredPermition);

        if (authorizationService.authorized(accessJWT, requiredPermition) == null)
            throw new SecurityException("Invalid permission.");
    }

    @Override
    public void verifyNullTokon(Permission requiredPermition) {
        throw new MissedAuthExeption("Null token is not acceptable.");
    }
}
