package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codeup.springboot_blog.daos.UserRepository;

@Controller
public class ProfileController {
    private UserRepository userDao;

    public ProfileController(UserRepository userDao){
        this.userDao = userDao;
    }

    @GetMapping("/pro/{id}")
    public String showUserProfile(@PathVariable long id, Model model){
//        User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if(loggedIn.getId() != id){
//            model.addAttribute("isYou", false);
//        } else {
//            model.addAttribute("isYou", true);
//        }
        User userInQuestion = userDao.getOne(id);

        model.addAttribute("name", userInQuestion.getUsername());

        model.addAttribute("id", id);
        return "profile";
    }
}

