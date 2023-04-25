package me.mppombo.synchronyapi.dto.imgur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Models the JSON response returned by Imgur's API upon a POST image upload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PostOkDto(boolean success, int status, ImgurDataDto data) { }
