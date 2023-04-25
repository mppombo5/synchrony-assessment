package me.mppombo.synchronyapi.exception;

import me.mppombo.synchronyapi.assembler.error.ImgurBadRequestModelAssembler;
import me.mppombo.synchronyapi.assembler.error.ImgurNotFoundModelAssembler;
import me.mppombo.synchronyapi.assembler.error.ImgurUnauthorizedModelAssembler;
import me.mppombo.synchronyapi.controller.ImgurController;
import me.mppombo.synchronyapi.dto.error.ImgurErrorDto;
import me.mppombo.synchronyapi.exception.imgur.ImgurBadRequestException;
import me.mppombo.synchronyapi.exception.imgur.ImgurDontOwnException;
import me.mppombo.synchronyapi.exception.imgur.ImgurNotFoundException;
import me.mppombo.synchronyapi.exception.imgur.ImgurUnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@ControllerAdvice
public class ImgurControllerExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(ImgurControllerExceptionHandler.class);

    private final ImgurNotFoundModelAssembler notFoundModelAssembler;
    private final ImgurBadRequestModelAssembler badReqModelAssembler;
    private final ImgurUnauthorizedModelAssembler unauthModelAssembler;

    public ImgurControllerExceptionHandler(
            ImgurNotFoundModelAssembler notFoundModelAssembler,
            ImgurBadRequestModelAssembler badReqModelAssembler,
            ImgurUnauthorizedModelAssembler unauthModelAssembler) {
        this.notFoundModelAssembler = notFoundModelAssembler;
        this.badReqModelAssembler = badReqModelAssembler;
        this.unauthModelAssembler = unauthModelAssembler;
    }


    @ExceptionHandler(ImgurNotFoundException.class)
    public ResponseEntity<EntityModel<ImgurErrorDto>> imageNotFoundHandler(ImgurNotFoundException ex, WebRequest req) {
        logger.warn("404 on Imgur");
        ImgurErrorDto notFoundBody = new ImgurErrorDto(
                new Date(),
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage(),
                req.getDescription(false));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundModelAssembler.toModel(notFoundBody));
    }

    @ExceptionHandler(ImgurBadRequestException.class)
    public ResponseEntity<EntityModel<ImgurErrorDto>> badRequestHandler(ImgurBadRequestException ex, WebRequest req) {
        logger.warn("Imgur POST request returned 400 Bad Request");
        ImgurErrorDto badReqBody = new ImgurErrorDto(
                new Date(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                req.getDescription(false));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badReqModelAssembler.toModel(badReqBody));
    }

    @ExceptionHandler(ImgurUnauthorizedException.class)
    public ResponseEntity<EntityModel<ImgurErrorDto>> unauthHandler(ImgurUnauthorizedException ex, WebRequest req) {
        logger.warn("Imgur DELETE request returned 403 Unauthorized");
        ImgurErrorDto unauthBody = new ImgurErrorDto(
                new Date(),
                HttpStatus.FORBIDDEN.name(),
                ex.getMessage(),
                req.getDescription(false));

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(unauthModelAssembler.toModel(unauthBody));
    }

    @ExceptionHandler(ImgurDontOwnException.class)
    public ResponseEntity<EntityModel<ImgurErrorDto>> dontOwnHandler(ImgurDontOwnException ex, WebRequest req) {
        logger.warn("Unauthorized request for image");
        var dontOwnDto = new ImgurErrorDto(
                new Date(),
                HttpStatus.UNAUTHORIZED.name(),
                ex.getMessage(),
                req.getDescription(false));

        var dontOwnModel = EntityModel.of(
                dontOwnDto,
                linkTo(methodOn(ImgurController.class).getImage(null, "")).withRel("imgurGet"));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dontOwnModel);
    }
}
