package me.mppombo.synchronyapi.dto.error;

import java.util.Date;

/*
 * Error response body for issues related to calling the Imgur public API.
 */
public record ImgurErrorDto(Date timestamp, String error, String message, String description) { }
