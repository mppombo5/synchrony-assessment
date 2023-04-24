package me.mppombo.synchronyapi.service;

import me.mppombo.synchronyapi.dto.imgur.DeleteOkBody;
import me.mppombo.synchronyapi.dto.imgur.PostOkBody;
import me.mppombo.synchronyapi.dto.imgur.GetOkBody;
import me.mppombo.synchronyapi.exception.imgur.ImgurBadRequestException;
import me.mppombo.synchronyapi.exception.imgur.ImgurNotFoundException;
import me.mppombo.synchronyapi.exception.imgur.ImgurUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ImgurService {
    private final Logger logger = LoggerFactory.getLogger(ImgurService.class);

    /*
     * In a production application, I'd know never to hardcode this and upload it to a repository. Basic research
     * indicates a better (more secure) solution would be to create a 'secrets' folder within src/resources and read API
     * keys from files in there which are included in .gitignore.
     * If you happen upon this, please don't get me banned from Imgur.
     */
    private final String clientId = "546c25a59c58ad7";
    private final String authHeader = "Client-ID " + clientId;
    private final WebClient webClient;

    public ImgurService(WebClient.Builder wcBuilder) {
        this.webClient = wcBuilder.baseUrl("https://api.imgur.com/3").build();
    }


    /*
     * Sends a GET request for the Imgur image specified by 'imgHash'.
     * In testing, GET requests to the URI '/images/{hash}' only return either a 200 or 404 response.
     */
    public GetOkBody getImgurImage(String imgHash) {
        GetOkBody getBody = webClient.get()
                .uri("/image/{imgHash}", imgHash)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        HttpStatus.NOT_FOUND::isSameCodeAs,
                        res -> Mono.error(new ImgurNotFoundException(imgHash)))
                .bodyToMono(GetOkBody.class)
                .block();

        logger.info("Successfully got imgHash='{}'", imgHash);
        return getBody;
    }

    /*
     * Sends a POST request with a multipart/form-data body which attempts to upload the supplied image to Imgur.
     * Imgur only ever seems to respond with a 200 for uploaded, or a 400 for a malformed request.
     */
    public PostOkBody uploadImgurImage(MultipartFile image, String title, String description) {
        var multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("image", image.getResource());
        if (title != null) {
            multipartBodyBuilder.part("title", title);
        }
        if (description != null) {
            multipartBodyBuilder.part("description", description);
        }
        var multipartData = multipartBodyBuilder.build();

        PostOkBody postBody = webClient.post()
                .uri("/upload")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData(multipartData))
                .retrieve()
                .onStatus(
                        HttpStatus.BAD_REQUEST::isSameCodeAs,
                        res -> Mono.error(new ImgurBadRequestException()))
                .bodyToMono(PostOkBody.class)
                .block();

        logger.info("Imgur gave 400 Bad Request on image upload");
        return postBody;
    }

    /*
     * Sends a DELETE request for the user-uploaded image specified by 'deletehash'.
     * Only ever gets a 200 for successful deletion or a 403 with an "Unauthorized" (even though 403 is technically
     * Forbidden).
     */
    public DeleteOkBody deleteImgurImage(String deletehash) {
        DeleteOkBody deleteBody = webClient.delete()
                .uri("/image/{deletehash}", deletehash)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        HttpStatus.FORBIDDEN::isSameCodeAs,
                        res -> Mono.error(new ImgurUnauthorizedException(deletehash)))
                .bodyToMono(DeleteOkBody.class)
                .block();

        logger.info("Successfully deleted Imgur image with deletehash='{}'", deletehash);
        return deleteBody;
    }
}
