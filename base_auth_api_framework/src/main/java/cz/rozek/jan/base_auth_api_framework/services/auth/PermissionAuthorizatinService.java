package cz.rozek.jan.base_auth_api_framework.services.auth;

import java.util.NoSuchElementException;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.springframework.stereotype.Service;

import cz.rozek.jan.base_auth_api_framework.permissions.Permission;
import cz.rozek.jan.base_auth_api_framework.services.jwt.IJwtService;

@Service("permissionAuthorizatinServic  e")
public class PermissionAuthorizatinService extends AuthorizationService {

    public PermissionAuthorizatinService(IJwtService jwtService) {
        super(jwtService);
    }
    
    @Override
    public String authorized(String jwt, Permission requiredPermition) {
        
        try {
            JwtClaims jwtClaims = jwtService.getClaimsFromToken(jwt, IAuthentizationService.PERMISSION_TOKEN);

            String appName = jwtClaims.getSubject();
            
            return appName;
        }  catch (MalformedClaimException e) {
            throw new SecurityException("Invalid JWT format.");
        } catch (NoSuchElementException e) {
            throw new SecurityException("Invalid User id.");
        }
    }
}
