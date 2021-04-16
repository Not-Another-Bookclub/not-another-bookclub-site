package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.*;
import com.codeup.springboot_blog.models.*;
import com.codeup.springboot_blog.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
public class MeetingController {
    private final PostRepository postDao;
    private final UserRepository userDao;
    private final EmailService emailService;
    private final CommentRepository commentDao;
    private final BookclubRepository bookclubDao;
    private final BookclubMembershipRepository bookclubmembershipDao;
    private final BookRepository bookDao;
    private final BookclubBookRepository bookclubBookDao;
    private final MeetingRepository meetingDao;


    public MeetingController(PostRepository postDao, UserRepository userDao, EmailService emailService, CommentRepository commentDao, BookclubRepository bookclubDao, BookclubMembershipRepository bookclubmembershipDao, BookRepository bookDao, BookclubBookRepository bookclubBookDao, MeetingRepository meetingDao) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.emailService = emailService;
        this.commentDao = commentDao;
        this.bookclubDao = bookclubDao;
        this.bookclubmembershipDao = bookclubmembershipDao;
        this.bookDao = bookDao;
        this.bookclubBookDao = bookclubBookDao;
        this.meetingDao = meetingDao;
    }


    @GetMapping("/bookclubs/{bookclubid}/meeting/{meetingid}")
    public String showOneMeeting(@PathVariable long bookclubid, @PathVariable long meetingid, Model model){
        Meeting meeting = meetingDao.getOne(meetingid);
        Bookclub bookclub = bookclubDao.getOne(bookclubid);
        if(meeting.getBookclub().getId() != bookclub.getId()) {
            model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                    "  That meeting and bookclub do not match, please check your link or try again.</div>");
            model.addAttribute("bookclubs", bookclubDao.findAll());
            return "bookclubs/index";
        }
        User user = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);
            List<BookclubMembership> memberships = bookclubmembershipDao.findBookclubMembershipsByUser(user);
            ArrayList<Bookclub> holder = new ArrayList<>();
            BookclubMembershipStatus active = BookclubMembershipStatus.valueOf("ACTIVE");
            int counter = 0;
            for (BookclubMembership membership : memberships) {
                if(!holder.contains(membership.getBookclub())){
                    if(membership.getStatus() == active){
                        holder.add(membership.getBookclub());
                    }
//                holder.add(membership.getBookclub());
                }
            }
            for (Bookclub bookclub1 : holder) {
                if (bookclub.getId() == bookclub1.getId()) {
                    counter++;
                }
            }
            if (counter > 0) {model.addAttribute("ismemberactive", true);}

            if (bookclub.getOwner().getId() == user.getId()) {model.addAttribute("isowner", true);}
        }

        model.addAttribute("meeting", meeting);
        model.addAttribute("bookclub", bookclub);
        return "meeting";
    }
}

