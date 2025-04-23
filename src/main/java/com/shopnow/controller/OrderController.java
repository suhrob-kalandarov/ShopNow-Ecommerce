package com.shopnow.controller;

import com.shopnow.model.Cart;
import com.shopnow.model.Order;
import com.shopnow.model.User;
import com.shopnow.model.enums.OrderStatus;
import com.shopnow.repository.UserRepository;
import com.shopnow.service.CartService;
import com.shopnow.service.CategoryService;
import com.shopnow.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserRepository userRepository;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService, CategoryService categoryService, UserRepository userRepository) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {
        String sessionId = getOrCreateSessionId(session);

        Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = (User) principalObj;
        model.addAttribute("loggedUser", user);

        List<Cart> cartItems = cartService.getCartItems(sessionId);

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        Double subtotal = cartService.getCartSubtotal(sessionId);
        Integer shipping = 10;
        Integer tax = (int) (subtotal * 0.1);
        Double total = subtotal + shipping + tax;

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

        String sessionId = getOrCreateSessionId(session);

        List<Cart> cartItems = cartService.getCartItems(sessionId);

        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(cartService.getCartSubtotal(sessionId).longValue());

        Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principalObj instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            User user = userRepository.findByEmail(username).orElse(null);
            if (user != null) {
                order.setUser(user);
            }
        }

        Order savedOrder = orderService.createOrder(order, cartItems);

        cartService.clearCart(sessionId);

        session.setAttribute("lastOrderId", savedOrder.getId());

        return "redirect:/orders/confirmation";
    }


    @GetMapping("/confirmation")
    public String showConfirmationPage(Model model, HttpSession session) {
        Integer orderId = (Integer) session.getAttribute("lastOrderId");

        if (orderId == null) {
            return "redirect:/";
        }

        Order order = orderService.getOrderById(orderId);

        if (order == null) {
            return "redirect:/";
        }

        String sessionId = getOrCreateSessionId(session);

        model.addAttribute("order", order);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("cartCount", cartService.getCartItemCount(sessionId));

        return "order-confirmation";
    }


    @GetMapping("/my-orders")
    public String showMyOrders(Model model, HttpSession session) {
        Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = (User) principalObj;

        if (user != null) {
            List<Order> orders = orderService.getOrdersByUserId(user.getId());

            String sessionId = getOrCreateSessionId(session);
            model.addAttribute("orders", orders);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("cartCount", cartService.getCartItemCount(sessionId));

            return "my-orders";
        }

        return "redirect:/login";
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