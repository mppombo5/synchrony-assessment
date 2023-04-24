package me.mppombo.synchronyapi.exception.imgur;

public class ImgurNotFoundException extends RuntimeException {
    private final String imgHash;

    public ImgurNotFoundException(String imgHash) {
        super("No image with imgHash='" + imgHash +"' found on Imgur");
        this.imgHash = imgHash;
    }

    public String getImgHash() {
        return imgHash;
    }
}
