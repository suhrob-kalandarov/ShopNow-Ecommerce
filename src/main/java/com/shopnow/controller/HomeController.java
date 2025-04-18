package com.shopnow.controller;

import com.shopnow.model.Category;
import com.shopnow.model.Product;
import com.shopnow.service.AttachmentService;
import com.shopnow.service.CartService;
import com.shopnow.service.CategoryService;
import com.shopnow.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CartService cartService;
    private final AttachmentService attachmentService;

    @Autowired
    public HomeController(ProductService productService, CategoryService categoryService,
                          CartService cartService, AttachmentService attachmentService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.cartService = cartService;
        this.attachmentService = attachmentService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        // Get all products
        List<Product> products = productService.getAllProducts();

        // Get all categories
        List<Category> categories = categoryService.getAllCategories();

        // Get cart count
        int cartCount = cartService.getCartItemCount(sessionId);

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("cartCount", cartCount);

        return "index";
    }

    @GetMapping("/category/{id}")
    public String categoryProducts(@PathVariable Integer id, Model model, HttpSession session) {
        // Ensure session ID exists
        String sessionId = getOrCreateSessionId(session);

        // Get products by category
        List<Product> products = productService.getProductsByCategory(id);

        // Get all categories
        List<Category> categories = categoryService.getAllCategories();

        // Get current category
        Category currentCategory = categoryService.getCategoryById(id).orElse(null);

        // Get cart count
        int cartCount = cartService.getCartItemCount(sessionId);

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", currentCategory);
        model.addAttribute("cartCount", cartCount);

        return "index";
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