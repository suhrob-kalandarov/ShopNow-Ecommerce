package com.shopnow.controller;

import com.shopnow.model.Attachment;
import com.shopnow.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageController {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @GetMapping("/images/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attachment Id:" + id));

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(attachment.getContent());
    }
}