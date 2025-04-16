package com.shopnow;

import com.shopnow.model.Attachment;
import com.shopnow.model.Category;
import com.shopnow.model.Product;
import com.shopnow.repository.AttachmentRepository;
import com.shopnow.repository.CategoryRepository;
import com.shopnow.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShopNowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopNowApplication.class, args);
    }


    @Bean
    @Transactional
    public CommandLineRunner initData(CategoryRepository categoryRepository,
                                      ProductRepository productRepository,
                                      AttachmentRepository attachmentRepository) {
        return args -> {
            // Check if we need to initialize data
            if (categoryRepository.count() == 0) {
                // Create categories
                Category electronics = categoryRepository.save(new Category("Electronics", "bi-laptop"));
                Category smartphones = categoryRepository.save(new Category("Smartphones", "bi-phone"));
                Category wearables = categoryRepository.save(new Category("Wearables", "bi-smartwatch"));
                Category audio = categoryRepository.save(new Category("Audio", "bi-headphones"));

                // Create products
                Product smartphone0 = Product.builder()
                        .name("Smartphone X")
                        .description("Latest model with advanced features")
                        .price(799)
                        .category(smartphones)
                        .build();
                productRepository.save(smartphone0);

                Product smartphone1 = Product.builder()
                        .name("iPhone 16 Pro Max")
                        .description("Latest model with advanced features")
                        .price(999)
                        .category(smartphones)
                        .build();
                productRepository.save(smartphone1);

                Product macbook = Product.builder()
                        .name("MacBook M2 Pro")
                        .description("Latest model with advanced features")
                        .price(999)
                        .category(electronics)
                        .build();
                productRepository.save(macbook);


                Product notebook = Product.builder()
                        .name("Lenovo Note")
                        .description("Latest model with advanced features")
                        .price(599)
                        .category(electronics)
                        .build();
                productRepository.save(notebook);

                // Add more products as needed

                // For attachments, use String paths instead of binary data
                createAttachment(macbook, "smartphone.jpg", attachmentRepository);
                createAttachment(notebook, "smartphone.jpg", attachmentRepository);
                createAttachment(smartphone0, "smartphone.jpeg", attachmentRepository);
                createAttachment(smartphone1, "smartphone.jpeg", attachmentRepository);
            }
        };
    }

    private void createAttachment(Product product, String filename, AttachmentRepository attachmentRepository) {
        // Store path instead of binary content
        Attachment attachment = Attachment.builder()
                .name(filename)
                .product(product)
                .build();

        attachmentRepository.save(attachment);
    }
}