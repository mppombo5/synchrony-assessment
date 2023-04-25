package me.mppombo.synchronyapi.service;

import me.mppombo.synchronyapi.dto.imgur.DeleteOkDto;
import me.mppombo.synchronyapi.dto.imgur.PostOkDto;
import me.mppombo.synchronyapi.dto.imgur.GetOkDto;
import me.mppombo.synchronyapi.models.ImgurImage;
import me.mppombo.synchronyapi.repository.ImgurImageRepository;
import me.mppombo.synchronyapi.utility.ImgurHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImgurService {
    private final Logger logger = LoggerFactory.getLogger(ImgurService.class);

    private final ImgurImageRepository imageRepository;
    private final ImgurHelper imgurHelper;

    public ImgurService(ImgurImageRepository imageRepository, ImgurHelper imgurHelper) {
        this.imageRepository = imageRepository;
        this.imgurHelper = imgurHelper;
    }


    /*
     * Sends a GET request for the Imgur image specified by 'imgHash'.
     * In testing, GET requests to the URI '/images/{hash}' only return either a 200 or 404 response.
     */
    public ImgurImage getImgurImage(String imgHash) {

        // TODO: ensure requesting user owns image before getting
        GetOkDto getBody = imgurHelper.getImage(imgHash);
        logger.info("Successful Imgur GET for imgHash='{}'", imgHash);

        ImgurImage gottenImage = ImgurImage.fromDataDto(getBody.data());
        logger.info("Sending {}", gottenImage);

        return gottenImage;
    }

    /*
     * Sends a POST request with a multipart/form-data body which attempts to upload the supplied image to Imgur.
     * Imgur only ever seems to respond with a 200 for uploaded, or a 400 for a malformed request.
     */
    public ImgurImage uploadImgurImage(MultipartFile image, String title, String description) {

        var multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("image", image.getResource());
        if (title != null) multipartBodyBuilder.part("title", title);
        if (description != null) multipartBodyBuilder.part("description", description);
        var multipartData = multipartBodyBuilder.build();

        PostOkDto postBody = imgurHelper.postImage(multipartData);
        logger.info("Successful Imgur POST");

        // TODO: associate posted image with logged-in user
        ImgurImage postedImage = ImgurImage.fromDataDto(postBody.data());

        return imageRepository.save(postedImage);
    }

    /*
     * Sends a DELETE request for the user-uploaded image specified by 'deletehash'.
     * Only ever gets a 200 for successful deletion or a 403 with an "Unauthorized" (even though 403 is technically
     * Forbidden).
     */
    public boolean deleteImgurImage(String deletehash) {

        // TODO: query logged-in user for deletehash, reject if they don't own it
        DeleteOkDto deleteBody = imgurHelper.deleteImage(deletehash);
        logger.info("Successful Imgur DELETE for deletehash='{}'", deletehash);

        Optional<ImgurImage> deletedImage = imageRepository.findByDeletehash(deletehash);
        if (deletedImage.isPresent()) {
            imageRepository.deleteById(deletedImage.get().getId());
            logger.info("Deleted {} w/ deletehash='{}' from database", deletedImage, deletehash);
        }

        return true;
    }
}
