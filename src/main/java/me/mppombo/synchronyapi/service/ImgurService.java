package me.mppombo.synchronyapi.service;

import me.mppombo.synchronyapi.dto.ImgurDeleteOkBody;
import me.mppombo.synchronyapi.dto.ImgurUploadOkBody;
import me.mppombo.synchronyapi.dto.ImgurViewOkBody;
import me.mppombo.synchronyapi.utility.error.GeneralErrorBody;
import me.mppombo.synchronyapi.utility.error.ImgurDeleteForbiddenErrorBody;
import me.mppombo.synchronyapi.utility.error.ImgurGetNotFoundErrorBody;
import me.mppombo.synchronyapi.utility.error.ImgurPostBadRequestErrorBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
    private final String authHeader = String.format("Client-ID %s", clientId);
    private final WebClient webClient;

    public ImgurService(WebClient.Builder wcBuilder) {
        this.webClient = wcBuilder.baseUrl("https://api.imgur.com/3").build();
    }


    /*
     * Sends a GET request for the Imgur image specified by 'imgHash'.
     * In testing, GET requests to the URI '/images/{hash}' only return either a 200 or 404 response.
     */
    public ResponseEntity<?> getImgurImage(String imgHash) {
        try {
            ImgurViewOkBody res = webClient.get()
                    .uri("/image/{imgHash}", imgHash)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ImgurViewOkBody.class)
                    .block();
            logger.info("Successfully GOT imgHash='{}'", imgHash);
            return ResponseEntity.ok(res);
        }
        catch (WebClientResponseException ex) {
            if (ex.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                logger.info("404 Not Found for imgHash='{}'", imgHash);
                var nfBody = new ImgurGetNotFoundErrorBody(imgHash);
                return ResponseEntity.status(nfBody.getStatus()).body(nfBody);
            }
            int status = ex.getStatusCode().value();
            logger.warn("Unexpected error {} for imgHash='{}'", status, imgHash);
            var errorBody = new GeneralErrorBody(status, ex.getStatusText());
            return ResponseEntity.status(status).body(errorBody);
        }
    }

    /*
     * Sends a POST request with a multipart/form-data body which attempts to upload the supplied image to Imgur.
     * Imgur only ever seems to respond with a 200 for uploaded, or a 400 for a malformed request.
     */
    public ResponseEntity<?> uploadImgurImage(MultipartFile image, String title, String description) {
        var multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("image", image.getResource());
        if (title != null) {
            multipartBodyBuilder.part("title", title);
        }
        if (description != null) {
            multipartBodyBuilder.part("description", description);
        }
        var multipartData = multipartBodyBuilder.build();

        try {
            ImgurUploadOkBody res = webClient.post()
                    .uri("/upload")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromMultipartData(multipartData))
                    .retrieve()
                    .bodyToMono(ImgurUploadOkBody.class)
                    .block();
            logger.info("Successfully POSTED image with ID={}", res.data().id());
            return ResponseEntity.ok(res);
        }
        catch (WebClientResponseException ex) {
            if (ex.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
                logger.info("400 Bad Request on image upload attempt");
                var badRequestErrorBody = new ImgurPostBadRequestErrorBody();
                return ResponseEntity.status(badRequestErrorBody.getStatus()).body(badRequestErrorBody);
            }
            int status = ex.getStatusCode().value();
            logger.warn("Unexpected error {} on attempted image upload", status);
            var errorBody = new GeneralErrorBody(status, ex.getStatusText());
            return ResponseEntity.status(status).body(errorBody);
        }
    }

    /*
     * Sends a DELETE request for the user-uploaded image specified by 'deletehash'.
     * Only ever gets a 200 for successful deletion or a 403 with an "Unauthorized" (even though 403 is technically
     * Forbidden).
     */
    public ResponseEntity<?> deleteImgurImage(String deletehash) {
        try {
            ImgurDeleteOkBody res = webClient.delete()
                    .uri("/image/{deletehash}", deletehash)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ImgurDeleteOkBody.class)
                    .block();
            logger.info("Successfully DELETED deletehash='{}'", deletehash);
            return ResponseEntity.ok(res);
        }
        catch (WebClientResponseException ex) {
            if (ex.getStatusCode().isSameCodeAs(HttpStatus.FORBIDDEN)) {
                logger.info("403 Forbidden/Unauthorized on deletehash='{}'", deletehash);
                var forbiddenBody = new ImgurDeleteForbiddenErrorBody(deletehash);
                return ResponseEntity.status(forbiddenBody.getStatus()).body(forbiddenBody);
            }
            int status = ex.getStatusCode().value();
            logger.warn("Unexpected error {} for deletehash='{}'", status, deletehash);
            var errorBody = new GeneralErrorBody(status, ex.getStatusText());
            return ResponseEntity.status(status).body(errorBody);
        }
    }
}
