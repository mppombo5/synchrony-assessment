package me.mppombo.synchronyapi.utility.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

/*
 * Used to send a 403 Forbidden when the DELETE call on the specified deletehash is unsuccessful.
 * 403 seems to be Imgur's catch-all for an unsuccessful delete regardless of whether the deletehash is actually
 * associated with an image. Fine by me, as it makes my life that much easier.
 *
 * The data returned by Imgur actually says "Unauthorized" instead of "Forbidden", despite the fact that it's a 403, so
 * that's what I tell the user.
 */
public class ImgurDeleteForbiddenErrorBody {
    private final int status = HttpStatus.FORBIDDEN.value();
    private final String message;
    @JsonIgnore
    private final String deletehash;

    public ImgurDeleteForbiddenErrorBody(String deletehash) {
        this.deletehash = deletehash;
        this.message = String.format("unable to delete image with deletehash '%s' from Imgur. you might not have permission, or it might not exist.", deletehash);
    }


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDeletehash() {
        return deletehash;
    }
}
