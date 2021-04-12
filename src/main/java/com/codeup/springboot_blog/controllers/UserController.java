package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.CommentRepository;
import com.codeup.springboot_blog.daos.UserRepository;
import com.codeup.springboot_blog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;
    private CommentRepository commentDao;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, CommentRepository commentDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.commentDao = commentDao;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        User user = new User();
        System.out.println("user.getUsername() = " + user.getUsername());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user, Model model){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
//        if (userDao.findByUsername(user.getUsername()) != null) {
//            model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
//                    "  That username is already in use, please select another.</div>");
//            model.addAttribute("user", user);
//            return "users/sign-up";
//        }
        userDao.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile/{username}/edit")
    public String profileEditRender(@PathVariable String username, Model model) {
        User loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userDao.findByUsername(loggedin.getUsername()));
        return "users/sign-up";
    }

    @PostMapping("profile/{username}/edit")
    public String profileEditSave(@ModelAttribute User user, @PathVariable String username, Model model) {
        User loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setId(loggedin.getId());
        userDao.save(user);
        return "redirect:/profile/" + user.getUsername();
    }
}