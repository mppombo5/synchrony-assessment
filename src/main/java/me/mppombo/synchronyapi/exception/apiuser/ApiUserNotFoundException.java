package me.mppombo.synchronyapi.exception.apiuser;

public class ApiUserNotFoundException extends RuntimeException {
    public ApiUserNotFoundException() {
        super("Requested user could not be found");
    }
}
