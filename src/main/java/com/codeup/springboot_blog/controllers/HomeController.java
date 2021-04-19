package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.UserRepository;
import com.codeup.springboot_blog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

//    private UserRepository userDao;
//
//    public HomeController(UserRepository userDao){
//
//    }




    @GetMapping("/")
    public String hello( Model model) {
        User user = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            model.addAttribute("user", user
//            );

//            return "redirect:profile";
        }
        model.addAttribute("user", user);

//        User userInQuestion = userDao.getOne(id);
//
//        User loggedin = new User();
//        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();}
//        if (loggedin.getId() == userInQuestion.getId()) {model.addAttribute("isowner", true);}


        return "home";




    }




    @GetMapping("/google")
    public String google() {return "googleBookIDBuild";}
}

