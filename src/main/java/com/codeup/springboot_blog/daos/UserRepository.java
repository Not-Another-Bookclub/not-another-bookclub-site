package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.Bookclub;
import com.codeup.springboot_blog.models.BookclubMembership;
import com.codeup.springboot_blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);

    User findByEmail(String email);
    List<User> findAllByBookclubMemberships(List<BookclubMembership> bookclubMemberships);


//    List<User> findAllByBookclubsIs(List<BookclubMembership> users);
//    @Query("SELECT user AS u JOIN bookclubs_users AS bu on u.id = bu.user_id JOIN bookclubs AS b ON b.id = bu.bookclub_id WHERE b.id = ?1 AND bu.status = '%:enumvalue%'")
//    @Query("SELECT User AS u FROM BookclubMembership WHERE BookclubMembership.bookclub.id = ?1 AND BookclubMembership.status = ?2")
//    List<User> membersofBookclub(long id, long status);
}
