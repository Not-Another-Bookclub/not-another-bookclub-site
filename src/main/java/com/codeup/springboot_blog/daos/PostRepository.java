package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
//    @Modifying
//    @Query(value="SELECT p FROM Post p WHERE p.title LIKE ?1")
    List<Post> findPostByTitleIsContainingOrBodyContaining(String term1, String term2);
    List<Post> findPostByTitleIsContaining(String term);
    List<Post> findPostByAuthor_Username(String username);
    List<Post> findPostsByTitle(String title);

//    Post deletePostById(long id);
//@Query("from Posts p where p.")

//    @Modifying
//    @Query(value = "update Posts p set p.title= title, p.body = body WHERE p.id = id")
//    void updatePostByID(String title, String body, long id);

//@SQLUpdate()

}
