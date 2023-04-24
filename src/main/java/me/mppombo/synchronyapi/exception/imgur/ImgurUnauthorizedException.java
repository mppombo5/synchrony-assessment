package me.mppombo.synchronyapi.exception.imgur;

public class ImgurUnauthorizedException extends RuntimeException {
    private final String deletehash;

    public ImgurUnauthorizedException(String deletehash) {
        super("Imgur returned 403 Permission Denied on deletehash='" + deletehash + "'");
        this.deletehash = deletehash;
    }

    public String getDeletehash() {
        return deletehash;
    }
}
