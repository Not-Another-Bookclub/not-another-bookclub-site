package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookByGoogleIDEquals(String googleID);
    List<Book> findAllByBookclubsIs(BookclubMembership bookclubMembership);
    List<Book> findAllByUsersEquals(User user);

}
