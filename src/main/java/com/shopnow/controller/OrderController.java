package com.shopnow.controller;

import com.shopnow.model.Cart;
import com.shopnow.model.Order;
import com.shopnow.model.OrderItem;
import com.shopnow.model.enums.OrderStatus;
import com.shopnow.service.CartService;
import com.shopnow.service.CategoryService;
import com.shopnow.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final CategoryService categoryService;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService, CategoryService categoryService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.categoryService = categoryService;
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {
        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        // Get cart items
        List<Cart> cartItems = cartService.getCartItems(sessionId);

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        // Calculate totals
        Integer subtotal = cartService.getCartSubtotal(sessionId);
        Integer shipping = 10;
        Integer tax = (int) (subtotal * 0.1);
        Integer total = subtotal + shipping + tax;

        // Create empty order for form binding
        Order order = new Order();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);
        model.addAttribute("order", order);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cartCount", cartService.getCartItemCount(sessionId));

        return "checkout";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute Order order, HttpSession session) {
        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        // Get cart items
        List<Cart> cartItems = cartService.getCartItems(sessionId);

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        // Set order properties
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(cartService.getCartSubtotal(sessionId).longValue());

        // Create order and order items
        Order savedOrder = orderService.createOrder(order, cartItems);

        // Clear cart after successful order
        cartService.clearCart(sessionId);

        // Store order ID in session for confirmation page
        session.setAttribute("lastOrderId", savedOrder.getId());

        return "redirect:/orders/confirmation";
    }

    @GetMapping("/confirmation")
    public String showConfirmationPage(Model model, HttpSession session) {
        // Get order ID from session
        Integer orderId = (Integer) session.getAttribute("lastOrderId");

        if (orderId == null) {
            return "redirect:/";
        }

        // Get order details
        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            return "redirect:/";
        }

        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        model.addAttribute("order", order);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cartCount", cartService.getCartItemCount(sessionId));

        return "order-confirmation";
    }

    @GetMapping("/my-orders")
    public String showMyOrders(Model model, HttpSession session) {
        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        // Get all orders (in a real app, you would filter by user)
        List<Order> orders = orderService.getAllOrders();

        model.addAttribute("orders", orders);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cartCount", cartService.getCartItemCount(sessionId));

        return "my-orders";
    }

    private String getOrCreateSessionId(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionId);
        }
        return sessionId;
    }
}