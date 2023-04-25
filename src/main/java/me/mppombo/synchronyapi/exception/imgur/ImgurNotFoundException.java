package me.mppombo.synchronyapi.exception.imgur;

/*
 * Ends up being a multipurpose exception for both when the image can't be found on Imgur, and when the image is not
 * present in the ImgurImage database since users can only view and delete their own images.
 */
public class ImgurNotFoundException extends RuntimeException {
    public ImgurNotFoundException() {
        super("Requested image could not be found");
    }
}
