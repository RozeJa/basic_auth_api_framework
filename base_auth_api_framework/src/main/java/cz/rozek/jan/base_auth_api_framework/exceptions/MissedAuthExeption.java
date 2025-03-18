package cz.rozek.jan.base_auth_api_framework.exceptions;

public class MissedAuthExeption extends SecurityException {
    public MissedAuthExeption(String message) {
        super(message);
    }
}
