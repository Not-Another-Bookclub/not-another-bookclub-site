package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.*;
import com.codeup.springboot_blog.models.*;
import com.codeup.springboot_blog.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Controller
public class PostController {
    private final PostRepository postDao;
    private final UserRepository userDao;
    private final EmailService emailService;
    private final CommentRepository commentDao;
    private final BookclubRepository bookclubDao;
    private final BookRepository bookDao;
    private final BookclubBookRepository bookclubbookDao;
    private final MeetingRepository meetingDao;
    private final BookclubMembershipRepository bookclubMembershipDao;



//    public PostController(PostRepository postDao, UserRepository userDao, EmailService emailService, CommentRepository commentDao){
//
//        this.postDao = postDao;
//        this.userDao = userDao;
//        this.emailService = emailService;
//        this.commentDao = commentDao;
//
//    }

    public PostController(PostRepository postDao,
                          UserRepository userDao,
                          EmailService emailService,
                          CommentRepository commentDao,
                          BookclubRepository bookclubDao,
                          BookRepository bookDao,
                          BookclubBookRepository bookclubbookDao,
                          MeetingRepository meetingDao,
                          BookclubMembershipRepository bookclubMembershipDao){

        this.postDao = postDao;
        this.userDao = userDao;
        this.emailService = emailService;
        this.commentDao = commentDao;
        this.bookclubDao = bookclubDao;
        this.bookDao = bookDao;
        this.bookclubbookDao = bookclubbookDao;
        this.meetingDao = meetingDao;
        this.bookclubMembershipDao = bookclubMembershipDao;

    }

@GetMapping("/posts")
    public String index(Model model) {
//    List<Post> posts = new ArrayList<>();
//    for (int i = 1; i<5; i++) {
//        posts.add(new Post(i, "Here's a title for this one", "Here's a bunch of text for it"
//
//        ));
//        model.addAttribute("posts", posts);
//    }
    List<Post> posts = postDao.findAll();
//    User loggedIn = new User();
//    loggedIn.setUsername(null);
    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {User loggedIn = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user",loggedIn);}
    Collections.reverse(posts);
    model.addAttribute("posts", posts);
    return "posts/index";
}

@GetMapping("bookclubs/{bookclubid}/posts/{id}")
    public String renderIndividualPost(@PathVariable long bookclubid, @PathVariable long id, Model model) {
//    Post post = new Post(id, "Here's a title for this detailed view", "Here's a bunch of text for it as well!");
//    model.addAttribute("post", post);
//    model.addAttribute("post", postDao.findAllById(id);
    Post post = postDao.getOne(id);
    if (post.getComments().size() > 0) {Collections.sort(post.getComments());};
    model.addAttribute("post", post);
    Bookclub bookclub = bookclubDao.getOne(bookclubid);

    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {User loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("loggedin",loggedin);
    if (loggedin.getId() == post.getAuthor().getId()) {
        model.addAttribute("isAuthor", true);
        if (loggedin.getId() == bookclub.getOwner().getId()) {
            model.addAttribute("isOwner", true);
        }}}

        //        This creates a list of googleIDs for the books and dates the bookclub will start reading them
        List<BookclubBook> clubbooks = bookclubbookDao.getAllByBookclub(bookclub);
        Collections.sort(clubbooks);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
        SimpleDateFormat html = new SimpleDateFormat("yyyy-MM-dd");
        List<String> books = new ArrayList<>();
        List<String> startdates = new ArrayList<>();
        List<String> startdateshtml = new ArrayList<>();
        List<String> finishdates = new ArrayList<>();
        List <String> finishdateshtml = new ArrayList<>();
        java.util.Date date = new java.util.Date();
        for (BookclubBook clubbook : clubbooks) {
            if (clubbook.getBook().getGoogleID() == post.getBook().getGoogleID()) {
            books.add(clubbook.getBook().getGoogleID());
            if (clubbook.getStartDate() != null) {
                startdates.add(sdf.format(clubbook.getStartDate()));
                startdateshtml.add(html.format(clubbook.getStartDate()));}
            else {startdates.add("Not started yet");
                startdateshtml.add(html.format(date));}
            if(meetingDao.findAllByBookclubEqualsAndBook_GoogleIDOrderByTimedate(bookclub, clubbook.getBook().getGoogleID()).size() > 0)
            {finishdates.add(sdf.format(meetingDao.findAllByBookclubEqualsAndBook_GoogleIDOrderByTimedate(bookclub,clubbook.getBook().getGoogleID()).get(meetingDao.findAllByBookclubEqualsAndBook_GoogleIDOrderByTimedate(bookclub,clubbook.getBook().getGoogleID()).size() -1).getTimedate()));
                finishdateshtml.add(html.format(meetingDao.findAllByBookclubEqualsAndBook_GoogleIDOrderByTimedate(bookclub,clubbook.getBook().getGoogleID()).get(meetingDao.findAllByBookclubEqualsAndBook_GoogleIDOrderByTimedate(bookclub,clubbook.getBook().getGoogleID()).size()-1).getTimedate()));}
            else {finishdates.add("Not finished yet");
                finishdateshtml.add(html.format(date));}
        }
    }

        System.out.println("bookclub.getId() = " + bookclub.getId());
        System.out.println("This is from the Post Controller");
        System.out.println("startdateshtml = " + startdateshtml);


        model.addAttribute("books", post.getBook());
        model.addAttribute("bookclub", bookclub);
        model.addAttribute("startdateshtml", startdateshtml);
        model.addAttribute("finishdateshtml", finishdateshtml);
        model.addAttribute("startdates", startdates);
        model.addAttribute("finishdates", finishdates);

//        Comment comment = new Comment();
//        comment.setComment("Type a comment to join the conversation.");
//        model.addAttribute("comment", comment);


    return "posts/show";
}

@PostMapping("/bookclubs/{bookclubid}/posts/delete")
    public String deleteIndividualPost(@RequestParam(name = "id") long id, @PathVariable long bookclubid, Model model) {
    postDao.deleteById(id);
    System.out.println("id = " + id);
    model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
            "  The post was successfully deleted. </div>");
    return "redirect:/posts";
    }

@GetMapping("/bookclubs/{id}/posts/create")
    public String createRender(@PathVariable long id, Model model) {
        BookclubMembershipStatus active = BookclubMembershipStatus.valueOf("ACTIVE");
        Bookclub bookclub = bookclubDao.getOne(id);
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {User loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("loggedin",loggedin);

            List<BookclubMembership> memberships = bookclubMembershipDao.findAllByBookclub(bookclub);
            List<User> members = new ArrayList<User>();
            for (BookclubMembership membership : memberships) {
                if (membership.getStatus() == active && membership.getUser().getId() == loggedin.getId()) {
                    members.add(membership.getUser()); model.addAttribute("isMember", true);
                }
            }
            if (members.isEmpty()){
                model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                        "  You must be an active member of this bookclub to create a post. </div>");
            }
        }
        else {
            model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                    "  You must be logged in to post create a post. </div>");
            return "users/login";
        }


        List<BookclubBook> bookclubBooks = bookclubbookDao.getAllByBookclub(bookclub);
        List<String> books = new ArrayList<>();
        for (BookclubBook bookclubook: bookclubBooks) {
            books.add(bookclubook.getBook().getGoogleID());
        }





        Post post = new Post();
    System.out.println("post.getId() = " + post.getId());
        model.addAttribute("bookclub", bookclub);
        model.addAttribute("books", books);
        model.addAttribute("post", post);
    return "posts/create";
}


@PostMapping("/bookclubs/{id}/posts/create")
//    public String createToDatabase(@RequestParam("title") String title, @RequestParam("body") String body, Model model) {
    public String createToDatabase(@ModelAttribute Post post, @PathVariable long id,@RequestParam(name = "book") String book, Model model) {
    User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    post.setAuthor(author);
    post.setBook(bookDao.findBookByGoogleIDEquals(book));
    post.setBookclub(bookclubDao.getOne(id));
    post.setCreateDate(new Date(Calendar.getInstance().getTime().getTime()));
    post.setModifyDate(new Date(Calendar.getInstance().getTime().getTime()));
    post.setId(0);
    postDao.save(post);
//    emailService.prepareAndSend(post, "Your post was successfully posted!", "You can view it at http://localhost:8080/posts/" + post.getId());
    model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
            "  The post was added successfully.</div>");
    return "redirect:/bookclubs/" + id +"/posts/" + post.getId();
}



    @GetMapping("/bookclubs/{bookclubid}/posts/{id}/edit")
    public String editIndividualPost (@PathVariable long bookclubid, @PathVariable long id, Model model){
        BookclubMembershipStatus active = BookclubMembershipStatus.valueOf("ACTIVE");
        Bookclub bookclub = bookclubDao.getOne(bookclubid);
        Post post = postDao.getOne(id);
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {User loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("loggedin",loggedin);

            List<BookclubMembership> memberships = bookclubMembershipDao.findAllByBookclub(bookclub);
            List<User> members = new ArrayList<User>();
            for (BookclubMembership membership : memberships) {
                if (membership.getStatus() == active && membership.getUser().getId() == loggedin.getId()) {
                    members.add(membership.getUser()); model.addAttribute("isMember", true);
                }
            }
            if (members.isEmpty()){
                model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                        "  You must be an active member of this bookclub to edit a post. </div>");
            }
            if(loggedin.getId() == post.getAuthor().getId()) {model.addAttribute("isAuthor", true);}
            else {
                model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                        "  You cannot edit a post that doesn't belong to you. </div>");
            }
        }
        else {
            model.addAttribute("alert", "<div class=\"alert alert-warning\" role=\"alert\">\n" +
                    "  You must be logged in to edit a post. </div>");
            return "users/login";
        }



//        Bookclub bookclub = bookclubDao.getOne(bookclubid);
        List<BookclubBook> bookclubBooks = bookclubbookDao.getAllByBookclub(bookclub);
        List<String> books = new ArrayList<>();
        for (BookclubBook bookclubook: bookclubBooks) {
            books.add(bookclubook.getBook().getGoogleID());
        }
        model.addAttribute("bookclub", bookclub);
        model.addAttribute("books", books);
        model.addAttribute("post", postDao.getOne(id));

        return "posts/edit";
    }

    @PostMapping("/bookclubs/{bookclubid}/posts/{id}/edit")
//    public String editSaveIndividualPost(@RequestParam(name = "id") long id, @RequestParam(name = "title") String title,
//                                         @RequestParam(name = "body") String body, Model model) {
    public String editSaveIndividualPost(@ModelAttribute Post post, @PathVariable long id, @PathVariable long bookclubid, Model model) {
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setAuthor(author);
//        Book book = bookDao.getOne(1L);
//        post.setBook(book);
        Bookclub bookclub = bookclubDao.getOne(bookclubid);
//        post.setBookclub(bookclub);
//        post.setId(id);
        postDao.save(post);
        model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
                "  The post was successfully updated. </div>");
        return "redirect:/bookclubs/" + bookclub.getId() + "/posts/" + post.getId();
    }

@GetMapping("/search")
    public String searchPosts (@RequestParam(name = "search") String terms, Model model) {
//    model.addAttribute("posts", postDao.findPostByTitleIsContaining(terms));
    model.addAttribute("posts", postDao.findPostByTitleIsContainingOrBodyContaining(terms, terms));
    model.addAttribute("search", terms);
    return "posts/index";
}

//@GetMapping("/profile/{username}")
//    public String userPosts(@PathVariable String username, Model model) {
//    User loggedin = new User();
//    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
//        loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }
//        List <Post> posts = postDao.findPostByAuthor_Username(username);
//        model.addAttribute("user", userDao.findByUsername(username));
//        model.addAttribute("posts", posts);
//        model.addAttribute("username", username);
//        if (username.equalsIgnoreCase(loggedin.getUsername())) {
//            model.addAttribute("owner", true);
//        }
//        if (posts.isEmpty()) {
//            model.addAttribute("posts", postDao.findAll());
//        }
//        return "posts/index";
//}


}