package com.shopnow.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;

@Controller
@RequestMapping("/images")
public class ImageController {

    @GetMapping("/placeholder.jpg")
    public ResponseEntity<byte[]> getPlaceholderImage() throws IOException {
        try {
            ClassPathResource imgFile = new ClassPathResource("static/images/placeholder.jpg");
            byte[] bytes = Files.readAllBytes(imgFile.getFile().toPath());
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        } catch (IOException e) {

            byte[] emptyImage = new byte[0];
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(emptyImage);
        }
    }
}