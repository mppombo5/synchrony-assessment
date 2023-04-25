package me.mppombo.synchronyapi.controller;

import lombok.RequiredArgsConstructor;
import me.mppombo.synchronyapi.assembler.ImageDtoModelAssembler;
import me.mppombo.synchronyapi.dto.ImgurImageDto;
import me.mppombo.synchronyapi.dto.ImgurDeleteDto;
import me.mppombo.synchronyapi.model.ImgurImage;
import me.mppombo.synchronyapi.security.UserPrincipal;
import me.mppombo.synchronyapi.service.ImgurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/imgur")
public class ImgurController {
    private final static Logger logger = LoggerFactory.getLogger(ImgurController.class);

    private final ImgurService service;
    private final ImageDtoModelAssembler imageDtoModelAssembler;

    @GetMapping("/image/{imgHash}")
    public ResponseEntity<EntityModel<ImgurImageDto>> getImage(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String imgHash) {
        logger.info("Received Imgur GET request for imgHash='{}'", imgHash);

        ImgurImage gottenImage = service.getImgurImage(principal.getUsername(), imgHash);
        var imageModel = imageDtoModelAssembler.toModel(gottenImage.toDto());

        return ResponseEntity.ok(imageModel);
    }

    @PostMapping(path = "/upload",
                 consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EntityModel<ImgurImageDto>> uploadImage(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestPart MultipartFile image,
            @RequestPart(required = false) String title,
            @RequestPart(required = false) String description) {
        logger.info("Received Imgur POST request");

        ImgurImage postedImage = service.uploadImgurImage(principal.getUsername(), image, title, description);
        var imageModel = imageDtoModelAssembler.toModel(postedImage.toDto());

        return ResponseEntity.created(imageModel.getLink("self").get().toUri()).body(imageModel);
    }

    @DeleteMapping("/image/{deletehash}")
    public ResponseEntity<EntityModel<ImgurDeleteDto>> deleteImage(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String deletehash) {
        logger.info("Received Imgur DELETE request for deletehash='{}'", deletehash);

        service.deleteImgurImage(principal.getUsername(), deletehash);

        ImgurDeleteDto resDto = new ImgurDeleteDto(
                HttpStatus.OK.name(),
                "Imgur returned a successful status for deletehash='" + deletehash + "'");
        var deleteModel = EntityModel.of(
                resDto,
                linkTo(methodOn(ImgurController.class).getImage(null, "")).withRel("imgurGet"),
                linkTo(methodOn(ImgurController.class)
                        .uploadImage(null, null, null, null))
                        .withRel("imgurUpload"));

        return ResponseEntity.ok(deleteModel);
    }
}
