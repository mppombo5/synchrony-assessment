package me.mppombo.synchronyapi.exception;

public class ApiUserUsernameTakenException extends RuntimeException {
    private final String username;

    public ApiUserUsernameTakenException(String username) {
        super("Username '" + username + "' is already taken");
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
