package cz.rozek.jan.base_auth_api_framework.services.auth;

import cz.rozek.jan.base_auth_api_framework.User;
import cz.rozek.jan.base_auth_api_framework.exceptions.ValidationException;

public interface IAuthentizationService {

    String LOGIN_TOKEN = "login-token";
    String ACCESS_TOKEN = "access-token";
    String THRUST_TOKEN = "thrust_token";
    String PERMISSION_TOKEN = "permission_token";
    
    void register(User user) throws ValidationException;
    boolean activate(String code);
    String login(User user);
    boolean logout(String loginJwt);

    String getJWT(String loginJwt);
    String resetPassword(User user, String loginJWT);
}
