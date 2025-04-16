package com.shopnow.service;

import com.shopnow.model.Attachment;
import com.shopnow.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    public Optional<Attachment> getAttachmentById(Integer id) {
        return attachmentRepository.findById(id);
    }

    public List<Attachment> getAttachmentsByProductId(Integer productId) {
        return attachmentRepository.findByProductId(productId);
    }
}