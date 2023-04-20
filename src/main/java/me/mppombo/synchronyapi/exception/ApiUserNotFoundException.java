package me.mppombo.synchronyapi.exception;

public class ApiUserNotFoundException extends RuntimeException {
    public ApiUserNotFoundException(Long id) {
        super("Could not find user with ID " + id);
    }
}
