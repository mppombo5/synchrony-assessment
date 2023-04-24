package me.mppombo.synchronyapi.exception.imgur;

public class ImgurBadRequestException extends RuntimeException {
    public ImgurBadRequestException() {
        super("Imgur returned 400 Bad Request on the provided data");
    }
}
