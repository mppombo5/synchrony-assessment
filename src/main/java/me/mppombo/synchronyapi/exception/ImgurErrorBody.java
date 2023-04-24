package me.mppombo.synchronyapi.exception;

import java.util.Date;

/*
 * Error response body for issues related to calling the Imgur public API.
 */
public record ImgurErrorBody(Date timestamp, String error, String message, String description) { }
