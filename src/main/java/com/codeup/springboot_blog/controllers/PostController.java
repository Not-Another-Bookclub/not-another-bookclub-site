package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.CommentRepository;
import com.codeup.springboot_blog.daos.UserRepository;
import com.codeup.springboot_blog.models.Comment;
import com.codeup.springboot_blog.models.Post;
import com.codeup.springboot_blog.daos.PostRepository;
import com.codeup.springboot_blog.models.User;
import com.codeup.springboot_blog.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class PostController {
    private final PostRepository postDao;
    private final UserRepository userDao;
    private final EmailService emailService;
    private final CommentRepository commentDao;


    public PostController(PostRepository postDao, UserRepository userDao, EmailService emailService, CommentRepository commentDao){

        this.postDao = postDao;
        this.userDao = userDao;
        this.emailService = emailService;
        this.commentDao = commentDao;
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
    Collections.reverse(posts);
    model.addAttribute("posts", posts);
    return "posts/index";
}

@GetMapping("posts/{id}")
    public String individualPost(@PathVariable long id, Model model) {
//    Post post = new Post(id, "Here's a title for this detailed view", "Here's a bunch of text for it as well!");
//    model.addAttribute("post", post);
//    model.addAttribute("post", postDao.findAllById(id);
    Post post = postDao.getOne(id);
    if (post.getComments().size() > 0) {Collections.sort(post.getComments());};
    model.addAttribute("post", post);
    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {User loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (loggedin.getId() == post.getAuthor().getId()) {
        model.addAttribute("owner", true);
//        Comment comment = new Comment();
//        comment.setComment("Type a comment to join the conversation.");
//        model.addAttribute("comment", comment);
    }}

    return "posts/show";
}

@PostMapping("/posts/delete")
    public String deleteIndividualPost(@RequestParam(name = "id") long id, Model model) {
    postDao.deleteById(id);
    System.out.println("id = " + id);
    model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
            "  The post was successfully deleted. </div>");
    return "redirect:/posts";
    }

@GetMapping("/posts/create")
    public String createRender(Model model) {
        model.addAttribute("post", new Post());
    return "posts/create";
}

@PostMapping("/posts/create")
//    public String createToDatabase(@RequestParam("title") String title, @RequestParam("body") String body, Model model) {
    public String createToDatabase(@ModelAttribute Post post, Model model) {
    User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    post.setAuthor(author);
    postDao.save(post);
    emailService.prepareAndSend(post, "Your post was successfully posted!", "You can view it at http://localhost:8080/posts/" + post.getId());
    model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
            "  The post was added successfully.</div>");
    return "redirect:/posts/" + post.getId();
}


@GetMapping("/posts/{id}/edit")
    public String editIndividualPost (@PathVariable long id, Model model){
    model.addAttribute("post", postDao.getOne(id));
    return "posts/create";
    }

@PostMapping("/posts/{id}/edit")
//    public String editSaveIndividualPost(@RequestParam(name = "id") long id, @RequestParam(name = "title") String title,
//                                         @RequestParam(name = "body") String body, Model model) {
public String editSaveIndividualPost(@ModelAttribute Post post, @PathVariable long id, Model model) {
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setAuthor(author);
        post.setId(id);
        postDao.save(post);
        model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
            "  The post was successfully updated. </div>");
        return "redirect:/posts";
}

@GetMapping("/search")
    public String searchPosts (@RequestParam(name = "search") String terms, Model model) {
//    model.addAttribute("posts", postDao.findPostByTitleIsContaining(terms));
    model.addAttribute("posts", postDao.findPostByTitleIsContainingOrBodyContaining(terms, terms));
    model.addAttribute("search", terms);
    return "posts/index";
}

@GetMapping("/profile/{username}")
    public String userPosts(@PathVariable String username, Model model) {
    User loggedin = new User();
    if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
        loggedin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
        List <Post> posts = postDao.findPostByAuthor_Username(username);
        model.addAttribute("user", userDao.findByUsername(username));
        model.addAttribute("posts", posts);
        model.addAttribute("username", username);
        if (username.equalsIgnoreCase(loggedin.getUsername())) {
            model.addAttribute("owner", true);
        }
        if (posts.isEmpty()) {
            model.addAttribute("posts", postDao.findAll());
        }
        return "posts/index";
}


}