package com.shopnow.controller;

import com.shopnow.model.User;
import com.shopnow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/settings")
    public String settingsPage(Authentication auth, Model model) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", user);
        return "user/settings";
    }

    @PostMapping("/settings")
    public String updateSettings(Authentication auth,
                                 @RequestParam String fullName,
                                 @RequestParam String phone,
                                 @RequestParam("photo") MultipartFile photo) throws IOException {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFullName(fullName);
        user.setPhone(phone);

        if (!photo.isEmpty()) {
            user.setPhoto(photo.getBytes());
        }

        userRepository.save(user);
        return "redirect:/settings?success";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
        return "redirect:/logout";
    }
}
