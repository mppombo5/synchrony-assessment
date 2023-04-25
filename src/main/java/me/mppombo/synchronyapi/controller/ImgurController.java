package me.mppombo.synchronyapi.controller;

import me.mppombo.synchronyapi.assembler.imgur.ok.ImgurDeleteModelAssembler;
import me.mppombo.synchronyapi.assembler.imgur.ok.ImgurGetModelAssembler;
import me.mppombo.synchronyapi.assembler.imgur.ok.ImgurPostModelAssembler;
import me.mppombo.synchronyapi.dto.imgur.DeleteOkBody;
import me.mppombo.synchronyapi.dto.imgur.GetOkBody;
import me.mppombo.synchronyapi.dto.imgur.PostOkBody;
import me.mppombo.synchronyapi.service.ImgurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
@RequestMapping("/imgur")
public class ImgurController {
    private final static Logger logger = LoggerFactory.getLogger(ImgurController.class);

    private final ImgurService service;
    private final ImgurGetModelAssembler getModelAssembler;
    private final ImgurPostModelAssembler postModelAssembler;
    private final ImgurDeleteModelAssembler deleteModelAssembler;

    public ImgurController(
            ImgurService service,
            ImgurGetModelAssembler getModelAssembler,
            ImgurPostModelAssembler postModelAssembler,
            ImgurDeleteModelAssembler deleteModelAssembler) {
        this.service = service;
        this.getModelAssembler = getModelAssembler;
        this.postModelAssembler = postModelAssembler;
        this.deleteModelAssembler = deleteModelAssembler;
    }


    @GetMapping("/image/{imgHash}")
    public ResponseEntity<EntityModel<GetOkBody>> getImage(@PathVariable String imgHash) {
        logger.info("Imgur GET request for imgHash='{}'", imgHash);
        var getBodyModel = getModelAssembler.toModel(service.getImgurImage(imgHash));

        return ResponseEntity.ok(getBodyModel);
    }

    @PostMapping(path = "/upload",
                 consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<EntityModel<PostOkBody>> uploadImage(
            @RequestPart MultipartFile image,
            @RequestPart(required = false) String title,
            @RequestPart(required = false) String description) {
        logger.info("Received Imgur POST request");
        var postBody = service.uploadImgurImage(image, title, description);
        String imgLink = postBody.data().link();
        var postBodyModel = postModelAssembler.toModel(postBody);

        return ResponseEntity.created(URI.create(imgLink)).body(postBodyModel);
    }

    @DeleteMapping("/image/{deletehash}")
    public ResponseEntity<EntityModel<DeleteOkBody>> deleteImage(@PathVariable String deletehash) {
        logger.info("Imgur DELETE request for deletehash='{}'", deletehash);
        var deleteBodyModel = deleteModelAssembler.toModel(service.deleteImgurImage(deletehash));

        return ResponseEntity.ok(deleteBodyModel);
    }
}
