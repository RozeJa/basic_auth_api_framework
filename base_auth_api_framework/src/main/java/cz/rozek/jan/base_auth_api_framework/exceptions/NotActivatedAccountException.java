package cz.rozek.jan.base_auth_api_framework.exceptions;

public class NotActivatedAccountException extends Exception {
    public NotActivatedAccountException(String message) {
        super(message);
    }
}
