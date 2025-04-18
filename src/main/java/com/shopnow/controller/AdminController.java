package com.shopnow.controller;

import com.shopnow.model.Attachment;
import com.shopnow.model.Category;
import com.shopnow.model.Product;
import com.shopnow.repository.AttachmentRepository;
import com.shopnow.repository.CategoryRepository;
import com.shopnow.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        return "admin/dashboard";
    }

    // Категориялар
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("category", new Category());
        return "admin/categories";
    }

    @PostMapping("/categories")
    public String saveCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Integer id, Model model) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "admin/category-form";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Integer id) {
        categoryRepository.deleteById(id);
        return "redirect:/admin/categories";
    }




    // Продуктлар
    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/products";
    }

    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @PostMapping("/products")
    @Transactional
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam("image") MultipartFile file) {
        // Агар файл юкланган бўлса
        if (!file.isEmpty()) {
            try {
                // Янги Attachment яратиш
                Attachment attachment = new Attachment();
                attachment.setName(file.getOriginalFilename());
                attachment.setContent(file.getBytes());

                attachmentRepository.save(attachment);

                product.setAttachment(attachment);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Продуктни сақлаш
        productRepository.save(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Integer id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productRepository.deleteById(id);
        return "redirect:/admin/products";
    }
}