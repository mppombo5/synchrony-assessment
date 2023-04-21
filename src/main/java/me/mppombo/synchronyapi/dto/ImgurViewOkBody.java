package me.mppombo.synchronyapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Models the JSON response returned by Imgur's API upon a GET request for an image.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ImgurViewOkBody(boolean success, int status, ImgViewData data) {
    /*
     * Subset of the 'data' object included in the response.
     * Includes the following selected properties:
     * - id (str)
     * - link (str)
     * - title (nullable str)
     * - description (nullable str)
     * - type (str)
     * - views (int)
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ImgViewData(String id, String link, String title, String description, String type, int views) { }
}
