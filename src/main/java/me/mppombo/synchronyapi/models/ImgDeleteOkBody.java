package me.mppombo.synchronyapi.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Models the JSON object returned by Imgur's API upon a DELETE request.
 * Unlike the responses for GET and POST requests, the 'data' field is not an object but rather a boolean value.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ImgDeleteOkBody(boolean success, int status, boolean data) { }
