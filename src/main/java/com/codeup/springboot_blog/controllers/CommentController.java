package com.codeup.springboot_blog.controllers;

import com.codeup.springboot_blog.daos.CommentRepository;
import com.codeup.springboot_blog.daos.PostRepository;
import com.codeup.springboot_blog.daos.UserRepository;
import com.codeup.springboot_blog.models.Comment;
import com.codeup.springboot_blog.models.Post;
import com.codeup.springboot_blog.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommentController {
    private UserRepository userDao;
    private PasswordEncoder passwordEncoder;
    private CommentRepository commentDao;
    private PostRepository postDao;

    public CommentController(UserRepository userDao, PasswordEncoder passwordEncoder, CommentRepository commentDao, PostRepository postDao) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.commentDao = commentDao;
        this.postDao = postDao;
    }

    @PostMapping("/posts/{id}/comment")
//    public String editSaveIndividualPost(@RequestParam(name = "id") long id, @RequestParam(name = "title") String title,
//                                         @RequestParam(name = "body") String body, Model model) {
    public String editSaveComment(@RequestParam("comment") String comment, @PathVariable long id, Model model) {
//    public String editSaveComment(@ModelAttribute Comment comment, @PathVariable long id, Model model) {
//        if (newComment == null) {
//            return "redirect:/posts/" + id;
//        }
//        else {
            System.out.println("At least it started the comment process");
            Comment addComment = new Comment();
            System.out.println("comment = " + comment);
//            Comment addComment = comment;
            addComment.setComment(comment);
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        addComment.setAuthor(author);
            System.out.println("author.getUsername() = " + author.getUsername());
            addComment.setPost(postDao.getOne(id));
            System.out.println("postDao.getOne(id).getTitle() = " + postDao.getOne(id).getTitle());
            commentDao.save(addComment);
//        model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
//                "  The post was successfully updated. </div>");
        return "redirect:/posts/" + id;
    }
//}

}
