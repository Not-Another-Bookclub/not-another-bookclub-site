package com.codeup.springboot_blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {
    @GetMapping("/pro/{id}")
    public String showUserProfile(@PathVariable long id, Model model){
        model.addAttribute("id", id);
        return "profile";
    }
}

