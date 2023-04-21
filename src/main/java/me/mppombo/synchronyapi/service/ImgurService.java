package me.mppombo.synchronyapi.service;

import me.mppombo.synchronyapi.models.ImgViewResponse;
import me.mppombo.synchronyapi.utility.error.GeneralErrorBody;
import me.mppombo.synchronyapi.utility.error.ImgurGetNotFoundErrorBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
            ImgViewResponse res = webClient.get()
                    .uri("/image/{imgHash}", imgHash)
                    .header(HttpHeaders.AUTHORIZATION, String.format("Client-ID %s", clientId))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ImgViewResponse.class)
                    .block();
            return ResponseEntity.ok(res);
        }
        catch (WebClientResponseException ex) {
            if (ex.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
                ImgurGetNotFoundErrorBody nfBody = new ImgurGetNotFoundErrorBody(imgHash);
                return ResponseEntity.status(nfBody.getStatus()).body(nfBody);
            }
            int statusCode = ex.getStatusCode().value();
            GeneralErrorBody errorBody = new GeneralErrorBody(statusCode, ex.getStatusText());
            return ResponseEntity.status(statusCode).body(errorBody);
        }
    }
}
