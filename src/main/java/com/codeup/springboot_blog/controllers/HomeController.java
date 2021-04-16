package com.codeup.springboot_blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
//public class HomeController {
//    @GetMapping("/")
//    public String hello() {
//        return "home";
//    }
//}

@Controller
public class HomeController {
    @GetMapping("/")
    public String hello() {
        return "home";
    }

    @GetMapping("/google")
    public String google() {return "googleBookIDBuild";}
}