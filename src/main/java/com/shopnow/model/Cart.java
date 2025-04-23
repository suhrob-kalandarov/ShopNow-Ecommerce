package com.shopnow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Column(name = "session_id")
    private String sessionId;

    // Calculated field (not stored in database)
    @Transient
    public Integer getSubtotal() {
        return product != null ? (int) (product.getPrice() * quantity) : 0;
    }
}