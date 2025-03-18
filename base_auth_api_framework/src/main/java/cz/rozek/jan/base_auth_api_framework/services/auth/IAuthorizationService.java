package cz.rozek.jan.base_auth_api_framework.services.auth;

import cz.rozek.jan.base_auth_api_framework.permissions.Permission;

public interface IAuthorizationService {

    static String ROLES = "roles";

    String authorized(String jwt, Permission requiredPermition);

    String getUserId(String jwt);

}
