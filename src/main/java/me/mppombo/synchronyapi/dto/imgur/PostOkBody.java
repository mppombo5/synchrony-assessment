package me.mppombo.synchronyapi.dto.imgur;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
 * Models the JSON response returned by Imgur's API upon a POST image upload.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PostOkBody(boolean success, int status, ImgUploadData data) {
    /*
     * Subset of the 'data' object included in the response.
     * Includes the following selected properties out of the many included:
     * - id (str)
     * - deletehash (str)
     * - link (str)
     * - title (nullable str)
     * - description (nullable str)
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ImgUploadData(String id,
                                String deletehash,
                                String link,
                                String title,
                                String description,
                                String type) { }
}
