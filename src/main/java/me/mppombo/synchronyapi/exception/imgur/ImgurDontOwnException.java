package me.mppombo.synchronyapi.exception.imgur;

import lombok.Getter;

@Getter
public class ImgurDontOwnException extends RuntimeException {
    public ImgurDontOwnException() {
        super("You don't have permission to do that with this image.");
    }
}
