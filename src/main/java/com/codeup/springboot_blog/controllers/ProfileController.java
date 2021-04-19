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
        List<Bookclub> bookclubsOwned = bookclubDao.findBookclubsByOwnerId(id);
        List<Bookclub> loggedUsersBookclubs = new ArrayList<>();
        ArrayList<BookclubMembership> bookClubMemberships =  bookclubMembershipDao.findBookclubMembershipsByUser(userInQuestion);
        ArrayList<Bookclub> holder = new ArrayList<>();
        BookclubMembershipStatus active = BookclubMembershipStatus.valueOf("ACTIVE");
        User user = new User();

//        LOGGED IN USER
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);

//            IS LOGGED IN USER THE OWNER OF PROFILE
            if (user.getId() == userInQuestion.getId()) {
                model.addAttribute("isowner", true);
            } else {
                System.out.println("TEST");
                model.addAttribute("canInvite", true);
            }

            loggedUsersBookclubs =  bookclubDao.findBookclubsByOwnerId(user.getId());
            model.addAttribute("loggedUserBookclubs", loggedUsersBookclubs);
        }

        for (BookclubMembership membership : bookClubMemberships) {
            if(!holder.contains(membership.getBookclub())){
//                GET PROFILE'S ACTIVE MEMBERSHIPS
                if(membership.getStatus() == active){
                    holder.add(membership.getBookclub());
                }
            }
        }

//        ALL BOOKS USER ADDED?
        List<UserBook> userbooks = userbookDao.findAllByUser(userInQuestion);
        List<String> books = new ArrayList<>();
        for(UserBook userbook : userbooks) {
            books.add(userbook.getBook().getGoogleID());
        }

//        PASS IN INFO
        model.addAttribute("name", userInQuestion.getUsername());
        model.addAttribute("ownedClubs", bookclubsOwned);
        model.addAttribute("memberClubs", holder);
        model.addAttribute("id", id);
        model.addAttribute("books", books);
        model.addAttribute("userbooks", userbooks);

        return "profile";
    }
}

