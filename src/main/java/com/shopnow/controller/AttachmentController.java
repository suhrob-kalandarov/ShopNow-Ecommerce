package com.shopnow.controller;

import com.shopnow.model.Attachment;
import com.shopnow.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable Integer id) {
        Optional<Attachment> attachmentOpt = attachmentService.getAttachmentById(id);

        if (attachmentOpt.isPresent()) {
            Attachment attachment = attachmentOpt.get();
            HttpHeaders headers = new HttpHeaders();

            if (attachment.getName() != null) {
                if (attachment.getName().toLowerCase().endsWith(".jpg") ||
                        attachment.getName().toLowerCase().endsWith(".jpeg")) {
                    headers.setContentType(MediaType.IMAGE_JPEG);
                } else if (attachment.getName().toLowerCase().endsWith(".png")) {
                    headers.setContentType(MediaType.IMAGE_PNG);
                } else if (attachment.getName().toLowerCase().endsWith(".gif")) {
                    headers.setContentType(MediaType.IMAGE_GIF);
                } else {
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                }
            } else {
                headers.setContentType(MediaType.IMAGE_JPEG);
            }

            return new ResponseEntity<>(attachment.getContent(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}