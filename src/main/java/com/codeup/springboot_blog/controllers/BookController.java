package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.*;
import com.codeup.springboot_blog.models.*;
import com.codeup.springboot_blog.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
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


    public BookController(PostRepository postDao, UserRepository userDao, BookRepository bookDao, UserBookRepository userbookDao, BookclubBookRepository bookclubBookDao, BookclubRepository bookclubDao, BookclubMembershipRepository bookclubmembershipDao){

        this.postDao = postDao;
        this.userDao = userDao;
        this.bookDao = bookDao;
        this.userbookDao = userbookDao;
        this.bookclubBookDao = bookclubBookDao;
        this.bookclubDao = bookclubDao;
        this.bookclubmembershipDao = bookclubmembershipDao;
    }

    @GetMapping("book/add")
    public String addBookRender(Model model) {
        return "posts/index";
    }

    @PostMapping("book/add")
    public String addBook(@RequestParam(name = "path") String path, @RequestParam(name = "book") String google_id, @RequestParam(name = "bookclubid") Long bookclubid, Model model) {
        User loggedin;
        Book book;
        if(bookDao.findBookByGoogleIDEquals(google_id) == null){
        book = new Book(google_id);
        bookDao.save(book);}
        else {book = bookDao.findBookByGoogleIDEquals(google_id);}
        if (path.equalsIgnoreCase("user")) {

            UserBook userbook = new UserBook();
            userbook.setBook(book);
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userbook.setUser(loggedin);
            userbook.setStarted(new Date(Calendar.getInstance().getTime().getTime()));
            userbookDao.save(userbook);
            model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
                    "  Book successfully added to your reading list.</div>");
                return "redirect:/pro/" + loggedin.getId();}
            else {model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                    "  <strong>Warning!</strong> Book not saved, you must be logged in to add books to your profile.</div>");
            return "users/login";}

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
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("user", user);
            model.addAttribute("isowner", true);
            List<BookclubMembership> memberships = bookclubmembershipDao.findAllByBookclub(bookclub);
            List <User> members = new ArrayList<User>();
            for (BookclubMembership membership : memberships) {
                members.add(membership.getUser());
            }
            List <BookclubBook> clubbooks = bookclubBookDao.getAllByBookclub(bookclub);
            List<String> books = new ArrayList<>();
            for (BookclubBook clubbook : clubbooks) {
                books.add(clubbook.getBook().getGoogleID());
            }
            model.addAttribute("bookclub", bookclub);
            model.addAttribute("members", members);
            model.addAttribute("books", books);
            return "bookclubs/show";}

        else {model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                "  <strong>Warning!</strong> Encountered an unexpected situation - book not saved. Please contact the dev team. (not really this isn't implemented yet).</div>");
            return "users/login";}
    }
}
