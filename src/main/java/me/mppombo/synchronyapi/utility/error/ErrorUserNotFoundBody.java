package me.mppombo.synchronyapi.utility.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

/*
 * Used to send a 404 Not Found when someone tries to GET an ID that has not been registered.
 */
public class ErrorUserNotFoundBody {
    private final int status = HttpStatus.NOT_FOUND.value();
    private final String message;
    @JsonIgnore
    private final Long id;

    public ErrorUserNotFoundBody(Long id) {
        this.id = id;
        this.message = String.format("no user with ID=%d exists", this.id);
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
