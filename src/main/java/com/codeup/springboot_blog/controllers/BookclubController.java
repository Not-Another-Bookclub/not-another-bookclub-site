package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.*;
import com.codeup.springboot_blog.models.*;
import com.codeup.springboot_blog.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class BookclubController {
    private final PostRepository postDao;
    private final UserRepository userDao;
    private final EmailService emailService;
    private final CommentRepository commentDao;
    private final BookclubRepository bookclubDao;
    private final BookclubMembershipRepository bookclubmembershipDao;

    public BookclubController(PostRepository postDao, UserRepository userDao, EmailService emailService, CommentRepository commentDao, BookclubRepository bookclubDao, BookclubMembershipRepository bookclubmembershipDao) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.emailService = emailService;
        this.commentDao = commentDao;
        this.bookclubDao = bookclubDao;
        this.bookclubmembershipDao = bookclubmembershipDao;
    }

    @GetMapping("/bookclubs")
    public String showAllBookclubs(Model model) {
        List<Bookclub> bookclubs = bookclubDao.findAll();
        model.addAttribute("bookclubs", bookclubs);

        return "bookclubs/index";
    }

    @GetMapping("/bookclubs/create")
    public String createNewBookclubRender(Model model) {
        Bookclub bookclub = new Bookclub();
        bookclub.setIs_private(false);
        bookclub.setAccepting_members(true);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookclub.setOwner(user);
        model.addAttribute("bookclub", bookclub);

        return "bookclubs/create";
    }

    @PostMapping("bookclubs/create")
    public String createNewBookclubSave(@ModelAttribute Bookclub bookclub, Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookclub.setOwner(user);
        bookclubDao.save(bookclub);
       return "redirect:/bookclubs/" + bookclub.getId();
    }

    @GetMapping("bookclubs/{id}")
    public String viewSpecificBookclub(@PathVariable long id, Model model) {
        User user = new User();
        Boolean isNotMember = true;
        Boolean isOwner = false;
        ArrayList<User> holder = new ArrayList<>();
        ArrayList<User> pendingHolder = new ArrayList<>();
        Bookclub bookclub = bookclubDao.getOne(id);
        BookclubMembershipStatus active = BookclubMembershipStatus.valueOf("ACTIVE");
        BookclubMembershipStatus pending = BookclubMembershipStatus.valueOf("PENDING");
//        BookclubMembershipStatus decline = BookclubMembershipStatus.valueOf("DECLINE");

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);

            ArrayList<BookclubMembership> bookClubMemberships =  bookclubmembershipDao.findBookclubMembershipsByUser(user);

            if(bookclub.getOwner().getId() == user.getId()){

                isOwner = true;
                ArrayList<BookclubMembership> forFiltereing = bookclubmembershipDao.findBookclubMembershipsByBookclub(bookclub);

                for(BookclubMembership membership : forFiltereing){
                    if(membership.getStatus() == pending){
                        User pendingUser = membership.getUser();
                        pendingHolder.add(pendingUser);
                    }
                }
            }

            for (BookclubMembership membership : bookClubMemberships) {

//                BookclubMembershipStatus active = BookclubMembershipStatus.valueOf("ACTIVE");
                if(membership.getUser() == user && membership.getStatus() == active){

                    holder.add(user);
                }

            }
        }

//        CHECK IF LOGGED USER IS MEMBER

        if(!holder.isEmpty()){
            isNotMember = false;
        }

        model.addAttribute("bookclub", bookclub);
        model.addAttribute("isNotMember", isNotMember);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("pendingUsers", pendingHolder);

        return "bookclubs/show";
    }

    @PostMapping("bookclubs/invite/{id}")
    public String requestToJoinBookclub(@PathVariable long id, Model model){
//        GOT USER
        User user = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);
        }

//        MAKE THE MEMBER_USER
        BookclubMembership newBookclubUser = new BookclubMembership();
        BookclubMembershipStatus pending = BookclubMembershipStatus.valueOf("PENDING");


        newBookclubUser.setBookclub(bookclubDao.getOne(id));
        newBookclubUser.setUser(user);
        newBookclubUser.setStatus(pending);
        newBookclubUser.setChangedDate(new Date(Calendar.getInstance().getTime().getTime()));
        newBookclubUser.setLastChangedBy(user);

        bookclubmembershipDao.save(newBookclubUser);

        System.out.println("Button works");
//        SEND EM BACK TO THE CLUB
        Bookclub bookclub = bookclubDao.getOne(id);
        model.addAttribute("bookclub", bookclub);
        return "bookclubs/show";
    }

    @PostMapping("bookclubs/invite/accept/{id}/prospectiveUserId")
    public String acceptRequestToJoinBookclub(@PathVariable long id, @PathVariable long prospectiveUserId, Model model){

        BookclubMembershipStatus active = BookclubMembershipStatus.valueOf("ACTIVE");
        User userToAccept = userDao.getOne(prospectiveUserId);
        Bookclub bookclub = bookclubDao.getOne(id);

        ArrayList<BookclubMembership> forFiltereing = bookclubmembershipDao.findBookclubMembershipsByBookclub(bookclub);

        BookclubMembership bookclubMembershipInQuestion = new BookclubMembership();

        for(BookclubMembership membership : forFiltereing){
            if(membership.getBookclub() == bookclub && membership.getUser().getId() == userToAccept.getId()){
                bookclubMembershipInQuestion = membership;
            }
        }

        bookclubMembershipInQuestion.setStatus(active);

        bookclubmembershipDao.save(bookclubMembershipInQuestion);


//        model.addAttribute("bookclub", bookclub);

        return "redirect:/bookclubs/" + id;
    }


}



