package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.CommentRepository;
import com.codeup.springboot_blog.daos.PostRepository;
import com.codeup.springboot_blog.daos.UserRepository;
import com.codeup.springboot_blog.models.Comment;
import com.codeup.springboot_blog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MeetingController {
    @GetMapping("/meeting/{id}")
    public String showOneMeeting(@PathVariable long id){
        return "meeting";
    }
}

