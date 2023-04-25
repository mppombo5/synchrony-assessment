package me.mppombo.synchronyapi.service;

import lombok.RequiredArgsConstructor;
import me.mppombo.synchronyapi.dto.imgur.PostOkDto;
import me.mppombo.synchronyapi.dto.imgur.GetOkDto;
import me.mppombo.synchronyapi.exception.imgur.ImgurDontOwnException;
import me.mppombo.synchronyapi.exception.imgur.ImgurNotFoundException;
import me.mppombo.synchronyapi.model.ApiUser;
import me.mppombo.synchronyapi.model.ImgurImage;
import me.mppombo.synchronyapi.repository.ApiUserRepository;
import me.mppombo.synchronyapi.repository.ImgurImageRepository;
import me.mppombo.synchronyapi.utility.ImgurHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImgurService {
    private final Logger logger = LoggerFactory.getLogger(ImgurService.class);

    private final ImgurImageRepository imageRepository;
    private final ApiUserRepository userRepository;
    private final ImgurHelper imgurHelper;

    /*
     * Sends a GET request for the Imgur image specified by 'imgHash'.
     * In testing, GET requests to the URI '/images/{hash}' only return either a 200 or 404 response.
     */
    public ImgurImage getImgurImage(String username, String imgHash) {

        var storedImage = imageRepository
                .findByImgurId(imgHash)
                .orElseThrow(ImgurNotFoundException::new);
        if (!username.equals(storedImage.getOwner().getUsername())) throw new ImgurDontOwnException();

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
    public ImgurImage uploadImgurImage(String ownerUsername, MultipartFile image, String title, String description) {

        var multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("image", image.getResource());
        if (title != null) multipartBodyBuilder.part("title", title);
        if (description != null) multipartBodyBuilder.part("description", description);
        var multipartData = multipartBodyBuilder.build();

        PostOkDto postBody = imgurHelper.postImage(multipartData);
        logger.info("Successful Imgur POST");

        // Associate this image with the logged-in user. We know they exist, since they couldn't have authenticated
        // otherwise.
        ImgurImage postedImage = ImgurImage.fromDataDto(postBody.data());
        ApiUser principalUser = userRepository.findByUsername(ownerUsername).get();
        postedImage.setOwner(principalUser);

        return imageRepository.save(postedImage);
    }

    /*
     * Sends a DELETE request for the user-uploaded image specified by 'deletehash'.
     * Only ever gets a 200 for successful deletion or a 403 with an "Unauthorized" (even though 403 is technically
     * Forbidden).
     */
    public void deleteImgurImage(String username, String deletehash) {

        var storedImage = imageRepository
                .findByDeletehash(deletehash)
                .orElseThrow(ImgurNotFoundException::new);
        if (!username.equals(storedImage.getOwner().getUsername())) throw new ImgurDontOwnException();
        imgurHelper.deleteImage(deletehash);
        logger.info("Successful Imgur DELETE for deletehash='{}'", deletehash);

        imageRepository.deleteById(storedImage.getId());
        logger.info("Deleted {} w/ deletehash='{}' from database", storedImage, deletehash);
    }

    public List<ImgurImage> getAllImagesForUser(ApiUser user) {
        return imageRepository.findByOwner(user);
    }
}
