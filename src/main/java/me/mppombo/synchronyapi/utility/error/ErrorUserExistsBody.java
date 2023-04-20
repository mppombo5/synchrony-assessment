package me.mppombo.synchronyapi.utility.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

/*
 * Used to send a 409 Conflict response when someone tries to register a new user with a username that is already
 * registered.
 */
public class ErrorUserExistsBody {
    private final int status = HttpStatus.CONFLICT.value();
    private final String message;   // descriptive message about what happened
    @JsonIgnore
    private final Long id;          // ID of the existing user

    public ErrorUserExistsBody(Long id, String username) {
        this.id = id;
        this.message = String.format("username '%s' is already taken", username);
    }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Long getId() {
        return id;
    }
}
