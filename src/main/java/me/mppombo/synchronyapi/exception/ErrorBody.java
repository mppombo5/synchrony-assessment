package me.mppombo.synchronyapi.exception;

import java.util.Date;

/*
 * Generic error response body for issues related to operations on ApiUsers.
 * Includes the HTTP status' name (but not code), a descriptive message, and the description given by the context's
 * WebRequest object.
 */
public record ErrorBody(Date timestamp, String error, String message, String description) { }
