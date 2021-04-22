package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.*;
import com.codeup.springboot_blog.models.*;
import com.codeup.springboot_blog.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public String createNewBookclubSave(@ModelAttribute Bookclub bookclub, Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bookclub.setOwner(user);
        bookclubDao.save(bookclub);
        return "redirect:/bookclubs/" + bookclub.getId();
    }

    @GetMapping("bookclubs/{id}")
    public String viewSpecificBookclub(@PathVariable long id, Model model) {
//        GLOBAL VARIABLES - ACCESSIBLE ANYWHERE IN ROUTE
        User user = new User();
        Boolean isNotMember = true;
        Boolean isOwner = false;
        Boolean isAccepting = true;
        Boolean isPrivate = false;
        Boolean isActiveUser = false;
        Boolean isNotActiveUser = true;
        ArrayList<Bookclub> holder = new ArrayList<>();
        ArrayList<User> pendingHolder = new ArrayList<>();
        Bookclub bookclub = bookclubDao.getOne(id);
        BookclubMembershipStatus active = BookclubMembershipStatus.valueOf("ACTIVE");
        BookclubMembershipStatus pending = BookclubMembershipStatus.valueOf("PENDING");

        if(bookclub.isAccepting_members() == false){
            isAccepting = false;
        }
        if(bookclub.isIs_private() == true){
            isPrivate = true;
        }


//        THIS BLOCK HANDLES IF USER IS LOGGED IN - BIG SECTION
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);

//            ALL MEMBERSHIPS LOGGED IN USER HAS
            ArrayList<BookclubMembership> bookClubMemberships = bookclubmembershipDao.findBookclubMembershipsByUser(user);
            ArrayList<Bookclub> checking = new ArrayList<>();
            for(BookclubMembership membership : bookClubMemberships){
                if(membership.getBookclub() == bookclub){
                    if(membership.getStatus() == active){
                        isActiveUser = true;
                    }
                }
            }

//            IF LOGGED IN USER OWNS THE BOOKCLUB WE ARE VIEWING
            if (bookclub.getOwner().getId() == user.getId()) {
                isOwner = true;

//                ALL MEMBERSHIPS ASSOCIATED WITH A BOOKCLUB
                ArrayList<BookclubMembership> forFiltereing = bookclubmembershipDao.findBookclubMembershipsByBookclub(bookclub);

//                pendingHOLDER GETS ALL USERS WHO ARE PENDING
                for (BookclubMembership membership : forFiltereing) {
                    if (membership.getStatus() == pending) {
                        User pendingUser = membership.getUser();
                        pendingHolder.add(pendingUser);
                    }
                }
            }

            for (BookclubMembership membership : bookClubMemberships) {

//                CHECKING IF LOGGED IN USER IS A MEMBER OF THE BOOKCLUB WE ARE VIEWING
                if (membership.getBookclub() == bookclub) {
                    holder.add(bookclub);
                }

            }
        }
//        END OF LOGGED IN USER LOGIC

//        GET ACTIVE MEMBERS ONLY
        List<BookclubMembership> memberships = bookclubmembershipDao.findAllByBookclub(bookclub);
        List<User> members = new ArrayList<User>();
        for (BookclubMembership membership : memberships) {
            if (membership.getStatus() == active) {
                members.add(membership.getUser());
            }
        }

        //        This creates a list of googleIDs for the books
        List<BookclubBook> clubbooks = bookclubBookDao.getAllByBookclub(bookclub);
        List<String> books = new ArrayList<>();
        for (BookclubBook clubbook : clubbooks) {
            books.add(clubbook.getBook().getGoogleID());
        }

//        GET ALL MEETINGS ASSOCIATED WITH THIS CLUB
        List<Meeting> meetings = meetingDao.findAllByBookclubEquals(bookclub);
        Collections.sort(meetings);

//       This creates and sorts (by date) a list of posts for this particular bookclub
        List<Post> posts = postDao.findAllByBookclubEquals(bookclub);
        Collections.sort(posts);

        
//        CHECK IF LOGGED IN USER IS MEMBER OF CLUB
        if (!holder.isEmpty()) {
            isNotMember = false;
        }

//        PASS INFO INTO THYMELEAF
        model.addAttribute("posts", posts);
        model.addAttribute("bookclub", bookclub);
        model.addAttribute("isNotMember", isNotMember);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("pendingUsers", pendingHolder);
        model.addAttribute("members", members);
        model.addAttribute("books", books);
        model.addAttribute("meetings", meetings);
        model.addAttribute("isAccepting", isAccepting);
        model.addAttribute("isPrivate", isPrivate);
        model.addAttribute("isActiveUser", isActiveUser);
        model.addAttribute("isNotActiveUser", isNotActiveUser);

        return "bookclubs/show";
    }

    @PostMapping("bookclubs/invite/{id}")
    public String requestToJoinBookclub(@PathVariable long id, Model model) {
//        GOT USER
        User user = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
        return "redirect:/bookclubs/" + id;
    }

    @PostMapping("bookclubs/invite/accept/{id}/{prospectiveUserId}")
    public String acceptRequestToJoinBookclub(@PathVariable long id, @PathVariable long prospectiveUserId, Model model) {

        BookclubMembershipStatus active = BookclubMembershipStatus.valueOf("ACTIVE");
        User userToAccept = userDao.getOne(prospectiveUserId);
        Bookclub bookclub = bookclubDao.getOne(id);

        ArrayList<BookclubMembership> forFiltereing = bookclubmembershipDao.findBookclubMembershipsByBookclub(bookclub);

        BookclubMembership bookclubMembershipInQuestion = new BookclubMembership();

        for (BookclubMembership membership : forFiltereing) {
            if (membership.getBookclub() == bookclub && membership.getUser().getId() == userToAccept.getId()) {
                bookclubMembershipInQuestion = membership;
            }
        }

        bookclubMembershipInQuestion.setStatus(active);

        bookclubmembershipDao.save(bookclubMembershipInQuestion);

        return "redirect:/bookclubs/" + id;
    }


    @PostMapping("bookclubs/invite/decline/{id}/{prospectiveUserId}")
    public String declineRequestToJoinBookclub(@PathVariable long id, @PathVariable long prospectiveUserId, Model model) {

        BookclubMembershipStatus decline = BookclubMembershipStatus.valueOf("DECLINED");
        User userToAccept = userDao.getOne(prospectiveUserId);
        Bookclub bookclub = bookclubDao.getOne(id);

        ArrayList<BookclubMembership> forFiltereing = bookclubmembershipDao.findBookclubMembershipsByBookclub(bookclub);

        BookclubMembership bookclubMembershipInQuestion = new BookclubMembership();

        for (BookclubMembership membership : forFiltereing) {
            if (membership.getBookclub() == bookclub && membership.getUser().getId() == userToAccept.getId()) {
                bookclubMembershipInQuestion = membership;
            }
        }

        bookclubMembershipInQuestion.setStatus(decline);

        bookclubmembershipDao.save(bookclubMembershipInQuestion);

        return "redirect:/bookclubs/" + id;
    }

    @PostMapping("bookclubs/inviteMany/{idOfTarget}/{inviterId}")
    public String inviteManyToJoinBookclub(@PathVariable long idOfTarget, @PathVariable long inviterId, @RequestParam (name="bookclubs") List<Long> bookclubs) {
        User targetUser = userDao.getOne(idOfTarget);
        User inviterUser = userDao.getOne(inviterId);
        BookclubMembershipStatus pending = BookclubMembershipStatus.valueOf("PENDING");

//        Make a loop to make a membership from each id provided.
        for(Long bookclubId : bookclubs){
            System.out.println(bookclubId);
            //        LOGIC TO MAKE A MEMBERSHIP
            Bookclub bookclub = bookclubDao.getOne(bookclubId);
            BookclubMembership newBookclubUser = new BookclubMembership();

            newBookclubUser.setBookclub(bookclub);
            newBookclubUser.setUser(targetUser);
            newBookclubUser.setStatus(pending);
            newBookclubUser.setChangedDate(new Date(Calendar.getInstance().getTime().getTime()));
            newBookclubUser.setLastChangedBy(inviterUser);

            bookclubmembershipDao.save(newBookclubUser);
            //        STOP LOGIC TO MAKE A MEMBERSHIP
        }

        return "redirect:/pro/" + idOfTarget;
    }



}



