package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.CommentRepository;
import com.codeup.springboot_blog.daos.UserRepository;
import com.codeup.springboot_blog.models.User;
import com.codeup.springboot_blog.services.EmailService;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.callback.ConfirmationCallback;

@Controller
public class UserController {
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;
    private CommentRepository commentDao;
    private EmailService emailService;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, CommentRepository commentDao, EmailService emailService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.commentDao = commentDao;
        this.emailService = emailService;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("loggedin", loggedin);
            model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                    "  You are already loggedin as " + loggedin.getUsername() + ". </div>");
        }
        User user = new User();
        user.setIs_admin(false);
        user.setIs_private(false);
        model.addAttribute("user", user);
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@ModelAttribute User user, Model model){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);

        if (user.getAvatar_path().isEmpty()) {user.setAvatar_path("/img/user-solid.svg");}
        if (userDao.findByUsername(user.getUsername()) != null) {
            model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                    "  That username is already in use, please select another.</div>");
            model.addAttribute("user", user);
            return "users/sign-up";
        }
        if (userDao.findByEmail(user.getEmail()) != null) {
            model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                    "  That email address is already in use, please select another.</div>");
            model.addAttribute("user", user);
            return "users/sign-up";
        }

        userDao.save(user);
        return "redirect:/login";
    }

//    @GetMapping("/profile/{username}/edit")
//    public String profileEditRender(@PathVariable String username, Model model) {
//        User loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("user", userDao.findByUsername(loggedin.getUsername()));
//        return "users/edit";
//    }

//    @PostMapping("profile/{username}/edit")
//    public String profileEditSave(@ModelAttribute User user, @PathVariable String username, Model model) {
//        User loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String hash = passwordEncoder.encode(user.getPassword());
//        user.setPassword(hash);
//        user.setId(loggedin.getId());
//        userDao.save(user);
//        return "redirect:/profile/" + user.getUsername();
//    }

    @GetMapping("/forgot-username")
    public String forgotUsername(Model model){
        User user = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();}

        model.addAttribute("user", user);
        return "users/forgot-username";
    }

    @PostMapping("/forgot-username")
    public String retrieveUsername(@RequestParam(name ="email") String email, Model model, User user){
//        ModelAndView modelAndView1 = new ModelAndView("viewPage");
        User user1 = userDao.findByEmail(email);
        if (user1 != null){
//            System.out.println(email);
//            System.out.println(user1.getUsername());
            emailService.prepareAndSend(email,user1.getUsername(),user1.getUsername());

            model.addAttribute("email", user1.getUsername());


            model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
                    "  The email was successfully sent. </div>");

        }
//        modelAndView1.addObject("message",);
//        model.addAttribute("email", email);

        return "users/forgot-username";
    }
}