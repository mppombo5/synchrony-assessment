package me.mppombo.synchronyapi.utility.error;

/*
 * For those errors that are unexpected or not well-defined, such as an unexpected status code from an Imgur API call.
 */
public record GeneralErrorBody(int status, String message) { }
