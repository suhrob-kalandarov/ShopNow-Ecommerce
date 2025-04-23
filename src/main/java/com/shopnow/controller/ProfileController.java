package com.shopnow.controller;

import com.shopnow.model.User;
import com.shopnow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String showProfile(Model model, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId()).get();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User updatedUser, @RequestParam("photo") MultipartFile photo) {
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteAccount(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        userRepository.delete(user);
        return "redirect:/logout";
    }
}
