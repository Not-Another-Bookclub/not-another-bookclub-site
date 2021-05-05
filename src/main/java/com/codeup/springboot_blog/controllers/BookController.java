package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.*;
import com.codeup.springboot_blog.models.*;
import com.codeup.springboot_blog.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Controller
public class BookController {
    private final PostRepository postDao;
    private final UserRepository userDao;
    private final BookRepository bookDao;
    private final UserBookRepository userbookDao;
    private final BookclubBookRepository bookclubBookDao;
    private final BookclubRepository bookclubDao;
    private final BookclubMembershipRepository bookclubmembershipDao;


    public BookController(PostRepository postDao, UserRepository userDao, BookRepository bookDao, UserBookRepository userbookDao, BookclubBookRepository bookclubBookDao, BookclubRepository bookclubDao, BookclubMembershipRepository bookclubmembershipDao) {

        this.postDao = postDao;
        this.userDao = userDao;
        this.bookDao = bookDao;
        this.userbookDao = userbookDao;
        this.bookclubBookDao = bookclubBookDao;
        this.bookclubDao = bookclubDao;
        this.bookclubmembershipDao = bookclubmembershipDao;
    }

    @PostMapping("/book/add")
    public String addBook(@RequestParam(name = "path") String path, @RequestParam(name = "book") String google_id, @RequestParam(name = "bookclubid") Long bookclubid, Model model) {
        User loggedin;
        Book book;
        if (bookDao.findBookByGoogleIDEquals(google_id) == null) {
            book = new Book(google_id);
            bookDao.save(book);
        } else {
            book = bookDao.findBookByGoogleIDEquals(google_id);
        }
        if (path.equalsIgnoreCase("user")) {

            UserBook userbook = new UserBook();
            userbook.setBook(book);
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
                loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                userbook.setUser(loggedin);
                userbook.setStarted(new Date(Calendar.getInstance().getTime().getTime()));
                userbookDao.save(userbook);
                model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
                        "  Book successfully added to your reading list.</div>");
                return "redirect:/pro/" + loggedin.getId();
            } else {
                model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                        "  <strong>Warning!</strong> Book not saved, you must be logged in to add books to your profile.</div>");
                return "users/login";
            }

        }
        if (path.equalsIgnoreCase("bookclub")) {
            BookclubBook bookclubbook = new BookclubBook();
            bookclubbook.setBook(book);
            Bookclub bookclub = bookclubDao.getOne(bookclubid);
            bookclubbook.setBookclub(bookclub);
            bookclubbook.setStartDate(new Date(Calendar.getInstance().getTime().getTime()));
            bookclubBookDao.save(bookclubbook);
            model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
                    "  Book saved to your bookclub's reading list.</div>");

            return "redirect:/bookclubs/" + bookclub.getId();
        } else {
            model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                    "  <strong>Warning!</strong> Encountered an unexpected situation - book not saved. Please contact the dev team. (not really this isn't implemented yet).</div>");
            return "users/login";
        }
    }

    @PostMapping("/bookclubs/{id}/book/edit")
    public String bookEditBookclub(@RequestParam(name = "startDate") String startDate, @RequestParam(name = "book") String googleID, @PathVariable long id, Model model) {
        try {
            BookclubBook bookclubBook = new BookclubBook();
            bookclubBook = bookclubBookDao.getBookclubBookByBook_GoogleIDAndBookclub(googleID, bookclubDao.getOne(id));
            bookclubBook.setBook(bookDao.findBookByGoogleIDEquals(googleID));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");
//        DateTimeFormatter dtf = new DateTimeFormatter("yyyy-MM-dd HH:mm");
            bookclubBook.setStartDate(sdf.parse(startDate + " 12:01"));
            bookclubBook.setBookclub(bookclubDao.getOne(id));
            bookclubBookDao.save(bookclubBook);
            return "redirect:/bookclubs/" + id;
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date input");
        }
    }

    @PostMapping("/pro/{id}/book/edit")
    public String addBookRender(@RequestParam(name = "startDate") String startDate, @RequestParam(name = "finishDate") String finishDate, @RequestParam(name = "book") String googleID, @PathVariable long id, Model model) {
        try {
            UserBook bookclubBook = new UserBook();
            bookclubBook = userbookDao.findUserBookByBook_GoogleIDAndUserIs(googleID, userDao.getOne(id));
            bookclubBook.setBook(bookDao.findBookByGoogleIDEquals(googleID));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");
//        DateTimeFormatter dtf = new DateTimeFormatter("yyyy-MM-dd HH:mm");
            bookclubBook.setStarted(sdf.parse(startDate + " 12:01"));
            bookclubBook.setFinished(sdf.parse(finishDate + " 12:01"));
            userbookDao.save(bookclubBook);
            return "redirect:/pro/" + id;
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing date input");
        }
    }

    @GetMapping("/pro/{id}/bookshelf")
    public String bookshelfRender(@PathVariable long id, Model model) {
        User user = new User();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<UserBook> userbooks = userbookDao.findAllByUser(user);
            Collections.sort(userbooks);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
            SimpleDateFormat html = new SimpleDateFormat("yyyy-MM-dd");
            List<String> books = new ArrayList<>();
            List<String> startdates = new ArrayList<>();
            List<String> startdateshtml = new ArrayList<>();
            List<String> finishdates = new ArrayList<>();
            List<String> finishdateshtml = new ArrayList<>();
            java.util.Date date = new java.util.Date();
            for (UserBook userbook : userbooks) {
                books.add(userbook.getBook().getGoogleID());
                if (userbook.getStarted() != null) {
                    startdates.add(sdf.format(userbook.getStarted()));
                    startdateshtml.add(html.format(userbook.getStarted()));
                } else {
                    startdates.add("Not started yet");
                    startdateshtml.add(html.format(date));
                }
                if (userbook.getFinished() != null) {
                    finishdates.add(sdf.format(userbook.getFinished()));
                    finishdateshtml.add(html.format(userbook.getFinished()));
                } else {
                    finishdates.add("Not finished yet");
                    finishdateshtml.add(html.format(date));
                }

                model.addAttribute("books", books);
                model.addAttribute("startdateshtml", startdateshtml);
                model.addAttribute("finishdateshtml", finishdateshtml);
                model.addAttribute("startdates", startdates);
                model.addAttribute("finishdates", finishdates);
            }

        } else {
            model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                    "  <strong>Warning!</strong> You must be logged in to connect with others through your bookshelf.</div>");
            model.addAttribute("user", user);
            return "users/login";
        }

//        PASS IN INFO
        model.addAttribute("user", user);


        return "users/bookshelf";
    }

    @RequestMapping(value = "/pro/{id}/bookshelf/bookclub.json", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<Bookclub> bookclubSearchByBooks(@PathVariable long id, @RequestParam("gid") String googleid) throws Exception {
        List<BookclubBook> bookclubBooks = bookclubBookDao.getAllByBook_GoogleID(googleid);
        List<Bookclub> bookclubs = new ArrayList<>();
        for (BookclubBook bookclubBook : bookclubBooks) {
            bookclubs.add(bookclubBook.getBookclub());
        }

        return bookclubs;
    }

    @RequestMapping(value = "/pro/{id}/bookshelf/user.json", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    List<User> userSearchByBooks(@PathVariable long id, @RequestParam("gid") String googleid) throws Exception {
        List<UserBook> userBooks = userbookDao.findAllByBook_GoogleID(googleid);
        List<User> users = new ArrayList<>();
        for (UserBook userBook : userBooks) {
            users.add(userBook.getUser());
        }

        return users;
    }

    @PostMapping("/bookclubs/{id}/book/delete")
    public String deleteBookclubBook(@RequestParam(name = "book") String googleID, @PathVariable long id, Model model) {
        BookclubBook bookclubBook = bookclubBookDao.getBookclubBookByBook_GoogleIDAndBookclub(googleID, bookclubDao.getOne(id));
        bookclubBookDao.delete(bookclubBook);
        return "redirect:/bookclubs/" + id;
    }

    @PostMapping("/pro/{id}/book/delete")
    public String deleteUserBook(@RequestParam(name = "book") String googleID, @PathVariable long id, Model model) {
        UserBook userBook = userbookDao.findUserBookByBook_GoogleIDAndUserIs(googleID, userDao.getOne(id));
        userbookDao.delete(userBook);
        return "redirect:/pro/" + id;
    }
}