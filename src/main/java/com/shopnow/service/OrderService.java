package com.shopnow.service;

import com.shopnow.model.Cart;
import com.shopnow.model.Order;
import com.shopnow.model.OrderItem;
import com.shopnow.model.Product;
import com.shopnow.repository.OrderItemRepository;
import com.shopnow.repository.OrderRepository;
import com.shopnow.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Order createOrder(Order order, List<Cart> cartItems) {
        // Save order first to get ID
        Order savedOrder = orderRepository.save(order);

        // Create order items from cart items
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(BigDecimal.valueOf(product.getPrice()));
            orderItem.setSubtotal(BigDecimal.valueOf(cartItem.getSubtotal()));

            orderItemRepository.save(orderItem);
            savedOrder.getItems().add(orderItem);
        }

        return savedOrder;
    }

    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}