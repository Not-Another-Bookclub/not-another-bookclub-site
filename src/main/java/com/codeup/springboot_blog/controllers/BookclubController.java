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
    private final BookRepository bookDao;
    private final BookclubBookRepository bookclubBookDao;
    private final MeetingRepository meetingDao;

    public BookclubController(PostRepository postDao, UserRepository userDao, EmailService emailService, CommentRepository commentDao, BookclubRepository bookclubDao, BookclubMembershipRepository bookclubmembershipDao, BookRepository bookDao, BookclubBookRepository bookclubBookDao, MeetingRepository meetingDao) {
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
        ArrayList<Bookclub> holder = new ArrayList<>();
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
                if(membership.getBookclub() == bookclub){
//&& membership.getStatus() == active
                    holder.add(bookclub);
                }

            }
        }

//        CHECK IF LOGGED USER IS MEMBER
      
              List <BookclubMembership> memberships = bookclubmembershipDao.findAllByBookclub(bookclub);
              List <User> members = new ArrayList<User>();
               for (BookclubMembership membership : memberships) {
                   if(membership.getStatus() == active){
                       members.add(membership.getUser());
                   }
                }

        List <BookclubBook> clubbooks = bookclubBookDao.getAllByBookclub(bookclub);
        List<String> books = new ArrayList<>();
        for (BookclubBook clubbook : clubbooks) {
            books.add(clubbook.getBook().getGoogleID());
        }
//        List<User> members = new ArrayList<>();
//        List<BookclubMembership> bookclubMemberships = bookclub.getUsers();
//        for (BookclubMembership membership : bookclubMemberships){
//            members.add(membership.getUser());
//        }

        List<Meeting> meetings = meetingDao.findAllByBookclubEquals(bookclub);
        System.out.println(holder);
        if(!holder.isEmpty()){
            isNotMember = false;
        }

        model.addAttribute("bookclub", bookclub);
        model.addAttribute("isNotMember", isNotMember);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("pendingUsers", pendingHolder);
        model.addAttribute("members", members);
        model.addAttribute("books", books);
          
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

    @PostMapping("bookclubs/invite/accept/{id}/{prospectiveUserId}")
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

        return "redirect:/bookclubs/" + id;
    }


    @PostMapping("bookclubs/invite/decline/{id}/{prospectiveUserId}")
    public String declineRequestToJoinBookclub(@PathVariable long id, @PathVariable long prospectiveUserId, Model model){

        BookclubMembershipStatus decline = BookclubMembershipStatus.valueOf("DECLINED");
        User userToAccept = userDao.getOne(prospectiveUserId);
        Bookclub bookclub = bookclubDao.getOne(id);

        ArrayList<BookclubMembership> forFiltereing = bookclubmembershipDao.findBookclubMembershipsByBookclub(bookclub);

        BookclubMembership bookclubMembershipInQuestion = new BookclubMembership();

        for(BookclubMembership membership : forFiltereing){
            if(membership.getBookclub() == bookclub && membership.getUser().getId() == userToAccept.getId()){
                bookclubMembershipInQuestion = membership;
            }
        }

        bookclubMembershipInQuestion.setStatus(decline);

        bookclubmembershipDao.save(bookclubMembershipInQuestion);

        return "redirect:/bookclubs/" + id;
    }


}



