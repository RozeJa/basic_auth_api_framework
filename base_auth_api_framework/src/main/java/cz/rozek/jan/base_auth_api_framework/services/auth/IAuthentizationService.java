package cz.rozek.jan.base_auth_api_framework.services.auth;

import cz.rozek.jan.base_auth_api_framework.User;
import cz.rozek.jan.base_auth_api_framework.exceptions.ValidationException;

public interface IAuthentizationService {

    String LOGIN_TOKEN = "login-token";
    String ACCESS_TOKEN = "access-token";
    
    boolean register(User user) throws ValidationException;
    String activate(String code);
    String login(User user);
    boolean logout(String accessJwt);

    String getJWT(String accessJwt);
    String resetPassword(User user, String jwt);
}
