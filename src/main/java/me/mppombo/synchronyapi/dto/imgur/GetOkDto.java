package me.mppombo.synchronyapi.dto.imgur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Models the JSON response returned by Imgur's API upon a GET request for an image.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetOkDto(boolean success, int status, ImgurDataDto data) { }
