package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.BookclubMembershipRepository;
import com.codeup.springboot_blog.daos.BookclubRepository;
import com.codeup.springboot_blog.models.Bookclub;
import com.codeup.springboot_blog.models.BookclubMembership;
import com.codeup.springboot_blog.models.BookclubMembershipStatus;
import com.codeup.springboot_blog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codeup.springboot_blog.daos.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {
    private UserRepository userDao;
    private BookclubRepository bookclubDao;
    private BookclubMembershipRepository bookclubMembershipDao;

    public ProfileController(UserRepository userDao, BookclubRepository bookclubDao, BookclubMembershipRepository bookclubMembershipDao){
        this.userDao = userDao;
        this.bookclubDao = bookclubDao;
        this.bookclubMembershipDao = bookclubMembershipDao;
    }

    @GetMapping("/pro/{id}")
    public String showUserProfile(@PathVariable long id, Model model){
        User userInQuestion = userDao.getOne(id);

        List<Bookclub> bookclubsOwned = bookclubDao.findBookclubsByOwnerId(id);

        ArrayList<BookclubMembership> bookClubMemberships =  bookclubMembershipDao.findBookclubMembershipsByUser(userInQuestion);

        ArrayList<Bookclub> holder = new ArrayList<>();

        BookclubMembershipStatus active = BookclubMembershipStatus.valueOf("ACTIVE");

        for (BookclubMembership membership : bookClubMemberships) {
            if(!holder.contains(membership.getBookclub())){

                if(membership.getStatus() == active){
                    holder.add(membership.getBookclub());
                }

            }
        }

        model.addAttribute("name", userInQuestion.getUsername());
        model.addAttribute("ownedClubs", bookclubsOwned);
        model.addAttribute("memberClubs", holder);
        model.addAttribute("id", id);

        return "profile";
    }
}

