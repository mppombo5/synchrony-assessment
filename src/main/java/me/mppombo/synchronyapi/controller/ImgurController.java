package me.mppombo.synchronyapi.controller;

import me.mppombo.synchronyapi.service.ImgurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImgurController {
    private final static Logger logger = LoggerFactory.getLogger(ImgurController.class);

    private final ImgurService service;

    public ImgurController(ImgurService service) {
        this.service = service;
    }


    @GetMapping("/imgur/{imgHash}")
    public ResponseEntity<?> getImage(@PathVariable String imgHash) {
        logger.info("Imgur GET request for imgHash='{}'", imgHash);
        return service.getImgurImage(imgHash);
    }

    @PostMapping(path = "/imgur/upload",
                 consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadImage(
            @RequestPart MultipartFile image,
            @RequestPart(required = false) String title,
            @RequestPart(required = false) String description) {
        logger.info("Received Imgur POST request");
        return service.uploadImgurImage(image, title, description);
    }

    @DeleteMapping("/imgur/{deletehash}")
    public ResponseEntity<?> deleteImage(@PathVariable String deletehash) {
        logger.info("Imgur DELETE request for deletehash='{}'", deletehash);
        return service.deleteImgurImage(deletehash);
    }
}
