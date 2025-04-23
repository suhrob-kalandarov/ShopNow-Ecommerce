package com.shopnow.repository;

import com.shopnow.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    //List<Attachment> findByProductId(Integer productId);
}