package me.mppombo.synchronyapi.utility.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

/*
 * Used to send a 404 Not Found when the specified imgHash doesn't exist.
 */
public class ImgurGetNotFoundErrorBody {
    private final int status = HttpStatus.NOT_FOUND.value();
    private final String message;
    @JsonIgnore
    private final String imgHash;

    public ImgurGetNotFoundErrorBody(String imgHash) {
        this.imgHash = imgHash;
        this.message = String.format("image with imgHash '%s' not found", imgHash);
    }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getImgHash() {
        return imgHash;
    }
}
