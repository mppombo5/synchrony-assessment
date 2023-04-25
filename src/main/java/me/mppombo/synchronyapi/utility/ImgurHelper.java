package me.mppombo.synchronyapi.utility;

import me.mppombo.synchronyapi.dto.imgur.DeleteOkDto;
import me.mppombo.synchronyapi.dto.imgur.GetOkDto;
import me.mppombo.synchronyapi.dto.imgur.PostOkDto;
import me.mppombo.synchronyapi.exception.imgur.ImgurBadRequestException;
import me.mppombo.synchronyapi.exception.imgur.ImgurNotFoundException;
import me.mppombo.synchronyapi.exception.imgur.ImgurUnauthorizedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/*
 * Helper class which takes care of actually sending/receiving REST calls via WebClient.
 * Mostly exists so that ImgurService isn't so cluttered; can be thought of as ImgurService's "repository".
 */
@Component
public class ImgurHelper {

    /*
     * In a production application, I'd know never to hardcode this and upload it to a repository. Basic research
     * indicates a better (more secure) solution would be to create a 'secrets' folder within src/resources and read API
     * keys from files in there which are included in .gitignore.
     * If you happen upon this, please don't get me banned from Imgur.
     */
    private final String clientId = "546c25a59c58ad7";
    private final String authHeader = "Client-ID " + clientId;

    private final WebClient webClient;

    public ImgurHelper(WebClient.Builder wcBuilder) {
        this.webClient = wcBuilder.baseUrl("https://api.imgur.com/3").build();
    }


    public GetOkDto getImage(String imgHash) {
        return webClient.get()
                .uri("/image/{imgHash}", imgHash)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        HttpStatus.NOT_FOUND::isSameCodeAs,
                        res -> Mono.error(new ImgurNotFoundException(imgHash)))
                .bodyToMono(GetOkDto.class)
                .block();
    }

    public PostOkDto postImage(MultiValueMap<String, HttpEntity<?>> multipartBody) {
        return webClient.post()
                .uri("/upload")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromMultipartData(multipartBody))
                .retrieve()
                .onStatus(
                        HttpStatus.BAD_REQUEST::isSameCodeAs,
                        res -> Mono.error(new ImgurBadRequestException()))
                .bodyToMono(PostOkDto.class)
                .block();
    }

    public DeleteOkDto deleteImage(String deletehash) {
        return webClient.delete()
                .uri("/image/{deletehash}", deletehash)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(
                        HttpStatus.FORBIDDEN::isSameCodeAs,
                        res -> Mono.error(new ImgurUnauthorizedException(deletehash)))
                .bodyToMono(DeleteOkDto.class)
                .block();
    }
}
