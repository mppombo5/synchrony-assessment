package me.mppombo.synchronyapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.hateoas.server.core.Relation;

/*
 * Represents an image which is uploaded by an ApiUser. Contains image information along with its owner.
 * Fields include:
 * - id (str, named imgurId)
 * - deletehash (str)
 * - link (str)
 * - title (nullable str)
 * - description (nullable str)
 * - type (str)
 */
@Entity
@Relation(itemRelation = "image", collectionRelation = "images")
public class ImgurImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgurId;
    private String deletehash;
    private String link;
    private String title;
    private String description;
    private String type;

    protected ImgurImage() { }

    public ImgurImage(String imgurId, String deletehash, String link, String title, String description, String type) {
        this.imgurId = imgurId;
        this.deletehash = deletehash;
        this.link = link;
        this.title = title;
        this.description = description;
        this.type = type;
    }


    @Override
    public String toString() {
        return String.format("Image[id=%d, imgurId='%s', deletehash='%s', title='%s'",
                id, imgurId, deletehash, title);
    }

    public Long getId() {
        return id;
    }

    public String getImgurId() {
        return imgurId;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
