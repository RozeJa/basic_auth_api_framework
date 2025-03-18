package cz.rozek.jan.base_auth_api_framework.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
