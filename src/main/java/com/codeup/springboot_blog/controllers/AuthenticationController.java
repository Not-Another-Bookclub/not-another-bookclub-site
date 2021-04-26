package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        User user = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        model.addAttribute("user", user);


        return "users/login";
    }
}
