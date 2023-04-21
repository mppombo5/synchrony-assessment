package me.mppombo.synchronyapi.utility.error;

import org.springframework.http.HttpStatus;

/*
 * Used to send a 400 Bad Request when an Imgur POST request fails.
 */
public class ImgurPostBadRequestErrorBody {
    private final int status = HttpStatus.BAD_REQUEST.value();
    private final String message = "failed to upload provided image to Imgur";

    public ImgurPostBadRequestErrorBody() { }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
