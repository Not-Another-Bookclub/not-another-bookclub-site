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

    @GetMapping("/posts/{id}/comment")
    @ResponseBody
    public String redirectToLogin(@PathVariable long id){

        return "You must be logged in to comment on a post!";
    }

    @PostMapping("/bookclubs/{bookclubid}/posts/{id}/comment")
//    public String editSaveIndividualPost(@RequestParam(name = "id") long id, @RequestParam(name = "title") String title,
//                                         @RequestParam(name = "body") String body, Model model) {
    public String editSaveComment(@RequestParam("comment") String comment,@PathVariable long bookclubid ,@PathVariable long id, Model model) {
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
           if( author.getUsername().equals("anonymousUser")  || author.getUsername().equals("") ){

               return "redirect:/users/sign-up";
           }

              addComment.setAuthor(author);
              System.out.println("author.getUsername() = " + author.getUsername());
              addComment.setPost(postDao.getOne(id));
              System.out.println("postDao.getOne(id).getTitle() = " + postDao.getOne(id).getTitle());
              commentDao.save(addComment);
//        model.addAttribute("alert", "<div class=\"alert alert-success\" role=\"alert\">\n" +
//                "  The post was successfully updated. </div>");
              return "redirect:/bookclubs/" + bookclubid +"/posts/" + id;
          }


    @PostMapping("/bookclubs/{bookclubid}/posts/{id}/comment/delete")
    public String editSaveComment(@RequestParam("id") long commentid,@PathVariable long bookclubid ,@PathVariable long id, Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Comment comment = commentDao.getOne(commentid);
          if (user.getId() == comment.getAuthor().getId()) {
          commentDao.delete(comment);}
        else {model.addAttribute("alert", "<div class=\"alert alert-danger\" role=\"alert\">\n" +
                  "  You must be logged in as the author of a comment to delete. </div>");
              return "users/login";
        }
        }
    else{        model.addAttribute("alert", "<div class=\"alert alert-danger\" role=\"alert\">\n" +
                "  You must be logged in as the author of a comment to delete. </div>");
            return "users/login";
        }
    return "redirect:/bookclubs/" + bookclubid + "/posts/" + id;

    }

    }

