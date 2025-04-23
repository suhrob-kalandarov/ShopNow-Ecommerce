package com.shopnow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;

@Entity
@Table(name = "product_attachments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private byte[] content;

    @Transient
    public String getBase64Image() {
        if (content != null) {
            return Base64.getEncoder().encodeToString(content);
        }
        return null;
    }

    @Transient
    public String getContentType() {
        if (name != null) {
            if (name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg")) {
                return "image/jpeg";
            } else if (name.toLowerCase().endsWith(".png")) {
                return "image/png";
            } else if (name.toLowerCase().endsWith(".gif")) {
                return "image/gif";
            }
        }
        return "image/jpeg";
    }

    @Transient
    public String getBase64ImageUrl() {
        if (content != null) {
            return "data:" + getContentType() + ";base64," + getBase64Image();
        }
        return null;
    }
}