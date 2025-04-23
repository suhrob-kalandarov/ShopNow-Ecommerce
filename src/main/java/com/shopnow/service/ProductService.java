package com.shopnow.service;

import com.shopnow.model.Product;
import com.shopnow.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(Integer categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }
}