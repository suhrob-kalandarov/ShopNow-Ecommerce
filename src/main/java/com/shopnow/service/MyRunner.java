package com.shopnow.service;

import com.shopnow.model.User;
import com.shopnow.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public MyRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {

            User user = User.builder()
                    .fullName("Eshmat Toshmatov")
                    .email("eshmat@gmail.com")
                    .password(passwordEncoder.encode("root123"))
                    .build();

            userRepository.save(user);

            User admin = User.builder()
                    .fullName("Hikmat Nusratov")
                    .email("nusrat@gmail.com")
                    .password(passwordEncoder.encode("root123"))
                    .build();

            userRepository.save(admin);
        }
    }
}
