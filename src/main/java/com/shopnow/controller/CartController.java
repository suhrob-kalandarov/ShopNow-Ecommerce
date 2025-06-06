package com.shopnow.controller;

import com.shopnow.model.Cart;
import com.shopnow.service.CartService;
import com.shopnow.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CategoryService categoryService;

    @Autowired
    public CartController(CartService cartService, CategoryService categoryService) {
        this.cartService = cartService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        // Get cart items
        List<Cart> cartItems = cartService.getCartItems(sessionId);

        // Calculate subtotal
        Integer subtotal = cartService.getCartSubtotal(sessionId);

        // Calculate shipping (fixed at 10)
        Integer shipping = 10;

        // Calculate tax (10% of subtotal)
        Integer tax = (int) (subtotal * 0.1);

        // Calculate total
        Integer total = subtotal + shipping + tax;

        // Calculate estimated delivery date (7 days from now)
        LocalDate deliveryDate = LocalDate.now().plusDays(7);
        String formattedDeliveryDate = deliveryDate.format(DateTimeFormatter.ofPattern("MMMM d"));

        // Get cart count
        int cartCount = cartItems.size();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("tax", tax);
        model.addAttribute("total", total);
        model.addAttribute("deliveryDate", formattedDeliveryDate);
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "cart";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Integer productId, @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        // Add to cart
        cartService.addToCart(productId, quantity, sessionId);

        return "redirect:/";
    }

    @PostMapping("/update/{cartItemId}")
    public String updateCartItem(@PathVariable Long cartItemId, @RequestParam int quantity,
                                 HttpSession session) {
        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        // Update cart item quantity
        cartService.updateCartItemQuantity(cartItemId, quantity, sessionId);

        return "redirect:/cart";
    }

    @PostMapping("/remove/{cartItemId}")
    public String removeFromCart(@PathVariable Long cartItemId, HttpSession session) {
        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        // Remove from cart
        cartService.removeFromCart(cartItemId, sessionId);

        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        // Clear cart
        cartService.clearCart(sessionId);

        return "redirect:/cart";
    }

    // Обновите метод checkout в CartController
    @PostMapping("/checkout")
    public String checkout() {
        return "redirect:/orders/checkout";
    }

    /*@PostMapping("/checkout")
    public String checkout(HttpSession session) {
        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        // Process checkout (in a real application, this would create an order)
        cartService.clearCart(sessionId);

        // Redirect to a thank you page or order confirmation
        return "redirect:/";
    }*/

    private String getOrCreateSessionId(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionId);
        }
        return sessionId;
    }
}