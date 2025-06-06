package com.shopnow.repository;

import com.shopnow.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findBySessionId(String sessionId);
    Optional<Cart> findBySessionIdAndProductId(String sessionId, Integer productId);
    void deleteBySessionId(String sessionId);
    int countBySessionId(String sessionId);

    Cart findByProductIdAndSessionId(Integer productId, String sessionId);
}