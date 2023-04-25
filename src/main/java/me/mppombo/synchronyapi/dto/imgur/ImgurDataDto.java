package me.mppombo.synchronyapi.dto.imgur;

/*
 * Models the 'data' JSON sub-object that's returned by Imgur on GET and POST calls.
 * The data returned by the two methods are practically identical, with the only difference I could find being that POST
 * requests return an additional 'deletehash' field to be used on DELETE calls.
 */
public record ImgurDataDto(
        String id,
        String link,
        String deletehash,  // maybe null
        Long datetime,
        String title,
        String description,
        String type,
        Boolean animated,
        Integer height,
        Integer width,
        Integer size) { }
