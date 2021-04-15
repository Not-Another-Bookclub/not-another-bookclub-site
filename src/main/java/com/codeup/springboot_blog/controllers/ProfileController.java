package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.BookclubRepository;
import com.codeup.springboot_blog.models.Bookclub;
import com.codeup.springboot_blog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codeup.springboot_blog.daos.UserRepository;

import java.util.List;

@Controller
public class ProfileController {
    private UserRepository userDao;
    private BookclubRepository bookclubDao;

    public ProfileController(UserRepository userDao, BookclubRepository bookclubDao){
        this.userDao = userDao;
        this.bookclubDao = bookclubDao;
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

//        Need custom?
        List<Bookclub> bookclubsOwned = bookclubDao.findBookclubsByOwnerId(id);

        String message;

        if(bookclubsOwned.size() > 0){
            message = "You own a least one bookclub!";
        } else {
            message = "You own NO bookclubs";
        }

        model.addAttribute("name", userInQuestion.getUsername());

        model.addAttribute("ownedClubs", bookclubsOwned);

        model.addAttribute("id", id);
        return "profile";
    }
}

