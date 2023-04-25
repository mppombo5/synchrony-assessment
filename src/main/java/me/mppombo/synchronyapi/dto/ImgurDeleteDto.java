package me.mppombo.synchronyapi.dto;

/*
 * JSON object to be sent on successfully deleting an Imgur image.
 * Imgur does not return any image data upon a valid DELETE request, so we just tell the user they did a great job.
 */
public record ImgurDeleteDto(String status, String message) { }
