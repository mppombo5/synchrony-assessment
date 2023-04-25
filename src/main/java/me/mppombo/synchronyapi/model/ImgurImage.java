package me.mppombo.synchronyapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.mppombo.synchronyapi.dto.ImgurImageDto;
import me.mppombo.synchronyapi.dto.imgur.ImgurDataDto;

import java.util.Date;

/*
 * Represents the Image models stored in the H2 database which are uploaded by ApiUsers.
 *
 * Mostly contains fields which are included in the "data" part of the response object given in an Imgur API call with
 * some omissions, such as "account_url" and "account_id" which are usually null and irrelevant fields like "is_ad",
 * "bandwidth", or "nsfw" (lol).
 *
 * Images are associated with an ApiUser and are added+associated on successful POST image upload, and they're
 * removed/unassociated upon successful image DELETE call.
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ImgurImage {
    @Id
    @GeneratedValue
    private Long id;

    private String imgurId;
    private String imgurLink;
    private String deletehash;
    private Long datetime;   // upload time in seconds since epoch
    private String title;
    private String description;
    private String type;
    private Boolean animated;
    private Integer height;
    private Integer width;
    private Integer size;

    @Override
    public String toString() {
        return String.format("Image[id=%d, imgurId='%s', imgurLink='%s', type='%s', title='%s', description='%s']",
                id, imgurId, imgurLink, type, title, description);
    }

    public static ImgurImage fromDataDto(ImgurDataDto dto) {
        return new ImgurImage(
                0L,
                dto.id(),
                dto.link(),
                dto.deletehash(),
                dto.datetime(),
                dto.title(),
                dto.description(),
                dto.type(),
                dto.animated(),
                dto.height(),
                dto.width(),
                dto.size());
    }

    public ImgurImageDto toDto() {
        return new ImgurImageDto(
                imgurId,
                imgurLink,
                deletehash,
                new Date(datetime * 1000),  // datetime stored as seconds, Date takes milliseconds
                title,
                description,
                type,
                animated,
                height,
                width,
                size);
    }
}
