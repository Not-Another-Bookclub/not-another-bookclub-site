package com.codeup.springboot_blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
class HelloController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello from Spring!";
    }
    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable String name, Model model)     {
    model.addAttribute("aName",name.toUpperCase());
        return "hello";
    }

    @RequestMapping(path = "/increment/{number}", method = RequestMethod.GET)
    @ResponseBody
    public String addOne(@PathVariable int number) {
        return number + " plus one is " + (number + 1) + "!";
    }

    @GetMapping("/join")
    public String renderJoin() {
        return  "join";
    }

    @PostMapping("/join")
    public String submitJoin(@RequestParam(name = "cohort") String cohort, Model viewModel){
        viewModel.addAttribute("cohort", "Welcome to " + cohort + "!");
        return "join";
    }

}
