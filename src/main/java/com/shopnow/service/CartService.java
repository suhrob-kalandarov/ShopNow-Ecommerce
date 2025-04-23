package com.shopnow.service;

import com.shopnow.model.Cart;
import com.shopnow.model.Product;
import com.shopnow.repository.CartRepository;
import com.shopnow.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public List<Cart> getCartItems(String sessionId) {
        return cartRepository.findBySessionId(sessionId);
    }

    @Transactional
    public void addToCart(Integer productId, int quantity, String sessionId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            Optional<Cart> existingCartItem = cartRepository.findBySessionIdAndProductId(sessionId, productId);

            if (existingCartItem.isPresent()) {
                Cart cartItem = existingCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartRepository.save(cartItem);
            } else {
                Cart newCartItem = Cart.builder()
                        .product(product)
                        .quantity(quantity)
                        .sessionId(sessionId)
                        .build();
                cartRepository.save(newCartItem);
            }
        }
    }

    @Transactional
    public void updateCartItemQuantity(Long cartItemId, int quantity, String sessionId) {
        Optional<Cart> cartItemOptional = cartRepository.findById(cartItemId);
        if (cartItemOptional.isPresent() && cartItemOptional.get().getSessionId().equals(sessionId)) {
            Cart cartItem = cartItemOptional.get();
            if (quantity > 0) {
                cartItem.setQuantity(quantity);
                cartRepository.save(cartItem);
            } else {
                cartRepository.delete(cartItem);
            }
        }
    }

    @Transactional
    public void removeFromCart(Long cartItemId, String sessionId) {
        Optional<Cart> cartItemOptional = cartRepository.findById(cartItemId);
        if (cartItemOptional.isPresent() && cartItemOptional.get().getSessionId().equals(sessionId)) {
            cartRepository.delete(cartItemOptional.get());
        }
    }

    @Transactional
    public void clearCart(String sessionId) {
        cartRepository.deleteBySessionId(sessionId);
    }

    public int getCartItemCount(String sessionId) {
        return cartRepository.countBySessionId(sessionId);
    }

    public Double getCartSubtotal(String sessionId) {
        List<Cart> cartItems = cartRepository.findBySessionId(sessionId);
        return cartItems.stream()
                .mapToDouble(Cart::getSubtotal)
                .sum();
    }
}