package cz.rozek.jan.base_auth_api_framework.services.auth;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.rozek.jan.base_auth_api_framework.services.jwt.IJwtService;
import cz.rozek.jan.base_auth_api_framework.Role;
import cz.rozek.jan.base_auth_api_framework.exceptions.SecurityException;
import cz.rozek.jan.base_auth_api_framework.permissions.Permission;

@Service
public class AuthorizationService implements IAuthorizationService {

    protected IJwtService jwtService;

    @Autowired
    public AuthorizationService(IJwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public String authorized(String jwt, Permission requiredPermition) {
        Set<Role> roles = getRoles(jwt);

        return haveRolesPermission(roles, requiredPermition) ? getUserId(jwt) : null;
    }

    @Override
    public String getUserId(String jwt) {        
        try {
            JwtClaims jwtClaims = jwtService.getClaimsFromToken(jwt, IAuthentizationService.ACCESS_TOKEN);

            String userId = jwtClaims.getSubject();
            
            return userId;
        }  catch (MalformedClaimException e) {
            throw new SecurityException("Invalid JWT format.");
        } catch (NoSuchElementException e) {
            throw new SecurityException("Invalid User id.");
        }
    }

    private Set<Role> getRoles(String jwt) {
        try {
            JwtClaims jwtClaims = jwtService.getClaimsFromToken(jwt, IAuthentizationService.ACCESS_TOKEN);

            Set<Role> roles = new TreeSet<>();

            roles = jwtClaims.getClaimValue(IAuthorizationService.ROLES, roles.getClass());
            
            return roles;
        }  catch (MalformedClaimException e) {
            throw new SecurityException("Invalid JWT format.");
        } catch (NoSuchElementException e) {
            throw new SecurityException("Invalid User id.");
        }
    }

    public final boolean haveRolesPermission(Set<Role> roles, Permission permission) {
        for (Role role : roles) {
            if (role.containsPermission(permission)) {
                return true;
            }
        }

        return false;
    }
}