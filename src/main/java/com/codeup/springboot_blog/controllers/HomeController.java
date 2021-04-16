package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/")
    public String hello(Model model) {
        User user = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();}
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/google")
    public String google() {return "googleBookIDBuild";}
}

