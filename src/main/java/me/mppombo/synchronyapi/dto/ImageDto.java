package me.mppombo.synchronyapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.server.core.Relation;

import java.util.Date;

/*
 * The JSON object that is sent to the user when they request an image, and what is stored in each user's image list.
 */
@Relation(itemRelation = "image", collectionRelation = "images")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record ImageDto(
        String imgurId,
        String imgurLink,
        String deletehash,
        Date uploadDate,
        String title,
        String description,
        String type,
        Boolean animated,
        Integer height,
        Integer width,
        Integer size) { }
