package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.*;
import com.codeup.springboot_blog.models.*;
import com.codeup.springboot_blog.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
        Boolean isOwner = false;
        if(meeting.getBookclub().getId() != bookclub.getId()) {
            model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                    "  That meeting and bookclub do not match, please check your link or try again.</div>");
            model.addAttribute("bookclubs", bookclubDao.findAll());
            return "bookclubs/index";
        }
        User user = new User();
//        IF YOU ARE LOGGED IN
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);

            if(bookclub.getOwner().getId() == user.getId()){
                isOwner = true;
            }

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
        model.addAttribute("isOwner", isOwner);
        return "meeting";
    }

    @GetMapping("/bookclubs/{bookclubId}/meeting/create")
    public String createNewBookclubRender(@PathVariable long bookclubId ,Model model) {
        Bookclub bookclub = bookclubDao.getOne(bookclubId);
        List<BookclubBook> bookclubBooks = bookclubBookDao.getAllByBookclub(bookclub);
        List<String> books = new ArrayList<>();
        for (BookclubBook bookclubook: bookclubBooks) {
            books.add(bookclubook.getBook().getGoogleID());
        }


        Meeting meeting = new Meeting();
        meeting.setTimedate(new Date(Calendar.getInstance().getTime().getTime()));
        meeting.setBookclub(bookclub);

        model.addAttribute("bookclub", bookclub);
        model.addAttribute("books", books);
        model.addAttribute("meeting", meeting);

        return "meetings/create";
    }

    @PostMapping("/bookclubs/{bookclubId}/meeting/create")
    public String createNewBookclubPost(
            @ModelAttribute Meeting meeting,
            @PathVariable long bookclubId,
            @RequestParam (name="location") String location,
            @RequestParam (name="book") String book,
            @RequestParam (name="day") String day,
            @RequestParam (name="time") String time,
            Model model) throws ParseException {


        Location newLocation = Location.valueOf(location);
        Bookclub bookclub = bookclubDao.getOne(bookclubId);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        String convertCurrentDate = day;
        String convertTime = time;
        Date date = new Date();
        date = df.parse(convertCurrentDate + " " + convertTime);

        System.out.println(time);

        meeting.setLocation(newLocation);
        meeting.setBookclub(bookclub);
        meeting.setTimedate(date);

        System.out.println(book);

        meeting.setLocation(newLocation);

        System.out.println(bookDao.findBookByGoogleIDEquals(book));
        meeting.setBook(bookDao.findBookByGoogleIDEquals(book));


        meetingDao.save(meeting);
        return "redirect:/bookclubs/" + bookclubId +"/meeting/" + meeting.getId();
    }

//    EDITING MEETINGS

    @GetMapping("/bookclubs/{bookclubid}/meeting/{meetingId}/edit")
    public String editIndividualMeeting (
            @PathVariable long bookclubid,
            @PathVariable long meetingId,
            Model model){

        Bookclub bookclub = bookclubDao.getOne(bookclubid);
        List<BookclubBook> bookclubBooks = bookclubBookDao.getAllByBookclub(bookclub);
        List<String> books = new ArrayList<>();
        for (BookclubBook bookclubook: bookclubBooks) {
            books.add(bookclubook.getBook().getGoogleID());
        }

        Meeting meeting = meetingDao.getOne(meetingId);

        model.addAttribute("bookclub", bookclub);
        model.addAttribute("books", books);
        model.addAttribute("meeting", meeting);

        return "meetings/edit";

    }

    @PostMapping("/bookclubs/{bookclubid}/meeting/{meetingId}/edit")
//    public String editSaveIndividualPost(@RequestParam(name = "id") long id, @RequestParam(name = "title") String title,
//                                         @RequestParam(name = "body") String body, Model model) {
    public String editSaveIndividualMeeting(
            @PathVariable long bookclubid,
            @PathVariable long meetingId,
            @RequestParam (name="location") String location,
            @RequestParam (name = "address") String address,
            @RequestParam (name = "city") String city,
            @RequestParam (name="day") String day,
            @RequestParam (name="zipPlusFour") String zip,
            @RequestParam (name="time") String time,
            @RequestParam (name="book") String book,
            Model model) throws ParseException {

        Bookclub bookclub = bookclubDao.getOne(bookclubid);
        Meeting meeting = meetingDao.getOne(meetingId);
        Location newLocation = Location.valueOf(location);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm");
        String convertCurrentDate = day;
        String convertTime = time;
        Date date = new Date();
        date = df.parse(convertCurrentDate + " " + convertTime);

        meeting.setLocation(newLocation);
        meeting.setAddress(address);
        meeting.setCity(city);
        meeting.setTimedate(date);
        meeting.setZipPlusFour(zip);
        meeting.setBook(bookDao.findBookByGoogleIDEquals(book));

        meetingDao.save(meeting);

        return "redirect:/bookclubs/" + bookclub.getId() + "/meeting/" + meetingId;
    }
}

