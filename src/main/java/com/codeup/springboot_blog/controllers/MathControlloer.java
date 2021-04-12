package com.codeup.springboot_blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;

@Controller
public class MathControlloer {
    @RequestMapping(path = "/add/{number1}/and/{number2}", method = RequestMethod.GET)
    @ResponseBody
    public int add(@PathVariable int number1,@PathVariable int number2) {
        return number1 + number2;
    }

    @RequestMapping(path ="/subtract/{number1}/from/{number2}", method = RequestMethod.GET)
    @ResponseBody
    public int subtract(@PathVariable int number1, @PathVariable int number2) {
        return number2 - number1;
    }

    @RequestMapping(path ="/multiply/{number1}/and/{number2}", method = RequestMethod.GET)
    @ResponseBody
    public int multiply(@PathVariable int number1, @PathVariable int number2) {
        return number1 * number2;
    }

    @RequestMapping(path ="divide/{number1}/by/{number2}", method = RequestMethod.GET)
    @ResponseBody
    public int divide(@PathVariable int number1, @PathVariable int number2) {
        return number1 / number2;
    }

    @GetMapping("/roll-dice")
    public String displayRollDice() {
        return "dice";
    }

    @GetMapping("/roll-dice/{guess}")
    public String guessingGame(@PathVariable int guess, Model model) {

        List<Integer> actuals = new ArrayList<>();
        List<Boolean> corrects = new ArrayList<>();
        HashMap<Integer, Boolean> hash = new HashMap<>();
        int count = 0;
        for (int i = 0; i < 6; i++) {
            Random random = new Random();
            int actual = random.nextInt(5)+1;
            hash.put(actual,actual == guess);
            if (actual == guess) {count++;}
        }
        model.addAttribute("actuals", actuals);
        model.addAttribute("guess", guess);
        model.addAttribute("hash", hash);
        model.addAttribute("corrects", corrects);
        model.addAttribute("count", count);
//        model.addAttribute("guess", "You guessed " + guess + "; the actual number was: " + actual);
//        if (guess == actual) {
//            model.addAttribute("message", "Good job! You got it exactly right!");
//        } else {
//            model.addAttribute("message", "Oh well, you were only off by " + abs(actual - guess));
//        }
        return "dice";
    }

    @PostMapping("/roll-dice")
    public String guessingGamePost(@RequestParam(name = "guess") int guess) {

        Random random = new Random();
        int actual = random.nextInt(5) + 1;
        return "dice";
    }

}
