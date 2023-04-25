package me.mppombo.synchronyapi.dto.imgur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Models the JSON object returned by Imgur's API upon a DELETE request.
 * Unlike the responses for GET and POST requests, the 'data' field is not an object but rather a boolean value.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DeleteOkDto(boolean success, int status, boolean data) { }
