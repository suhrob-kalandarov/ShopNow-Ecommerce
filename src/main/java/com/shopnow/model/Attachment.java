package com.shopnow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Base64;

@Entity
@Table(name = "attachments")
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

    // Product билан боғланиш олиб ташланди, чунки энди OneToOne боғланиш Product томонидан бошқарилади

    // Base64 форматида расмни қайтарадиган метод
    @Transient
    public String getBase64Image() {
        if (content != null) {
            return Base64.getEncoder().encodeToString(content);
        }
        return null;
    }

    // Расм MIME типини аниқлаш
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
        return "image/jpeg"; // Стандарт қиймат
    }

    // Base64 форматида тўлиқ URL қайтарадиган метод
    @Transient
    public String getBase64ImageUrl() {
        if (content != null) {
            return "data:" + getContentType() + ";base64," + getBase64Image();
        }
        return null;
    }
}


/*
package com.shopnow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attachments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Lob
    @Column(columnDefinition = "bytea")
    private byte[] content;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

*/
/*
package com.shopnow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "attachments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @PrePersist
    public void generateName() {
        if (name == null) {
            name = UUID.randomUUID().toString();
        }
    }
}*/

