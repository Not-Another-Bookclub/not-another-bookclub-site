package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.Comment;
import com.codeup.springboot_blog.models.Post;
import com.codeup.springboot_blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    List<Comment> findAllByPostsOrderById(Post post);
}
