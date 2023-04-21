package me.mppombo.synchronyapi.controller;

import me.mppombo.synchronyapi.service.ImgurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ImgurController {
    private final static Logger logger = LoggerFactory.getLogger(ImgurController.class);

    private final ImgurService service;

    public ImgurController(ImgurService service) {
        this.service = service;
    }


    @GetMapping("/imgur/{imgHash}")
    public ResponseEntity<?> getImage(@PathVariable String imgHash) {
        return service.getImgurImage(imgHash);
    }

    /*
    @PostMapping("/imgur/upload")
    public ResponseEntity<?> uploadImage(@RequestBody)
     */
}