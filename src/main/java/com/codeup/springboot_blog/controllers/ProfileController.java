package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.BookclubMembershipRepository;
import com.codeup.springboot_blog.daos.BookclubRepository;
import com.codeup.springboot_blog.daos.UserBookRepository;
import com.codeup.springboot_blog.models.*;
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
    private UserBookRepository userbookDao;

    public ProfileController(UserRepository userDao, BookclubRepository bookclubDao, BookclubMembershipRepository bookclubMembershipDao, UserBookRepository userbookDao){
        this.userDao = userDao;
        this.bookclubDao = bookclubDao;
        this.bookclubMembershipDao = bookclubMembershipDao;
        this.userbookDao = userbookDao;
    }

    @GetMapping("/pro/{id}")
    public String showUserProfile(@PathVariable long id, Model model){
        User userInQuestion = userDao.getOne(id);

        User user = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);
        }

        List<Bookclub> bookclubsOwned = bookclubDao.findBookclubsByOwnerId(id);

        ArrayList<BookclubMembership> bookClubMemberships =  bookclubMembershipDao.findBookclubMembershipsByUser(userInQuestion);

        ArrayList<Bookclub> holder = new ArrayList<>();

        BookclubMembershipStatus active = BookclubMembershipStatus.valueOf("ACTIVE");

        User loggedin = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();}
        if (loggedin.getId() == userInQuestion.getId()) {model.addAttribute("isowner", true);}

        for (BookclubMembership membership : bookClubMemberships) {
            if(!holder.contains(membership.getBookclub())){

                if(membership.getStatus() == active){
                    holder.add(membership.getBookclub());
                }

            }
        }

        List<UserBook> userbooks = userbookDao.findAllByUser(userInQuestion);
        List<String> books = new ArrayList<>();
        for(UserBook userbook : userbooks) {
            books.add(userbook.getBook().getGoogleID());
        }

        model.addAttribute("name", userInQuestion.getUsername());
        model.addAttribute("ownedClubs", bookclubsOwned);
        model.addAttribute("memberClubs", holder);
        model.addAttribute("id", id);
        model.addAttribute("books", books);
        model.addAttribute("userbooks", userbooks);


        return "profile";
    }
}

