package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.Book;
import com.codeup.springboot_blog.models.Bookclub;
import com.codeup.springboot_blog.models.BookclubMembership;
import com.codeup.springboot_blog.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findBookByGoogleIDEquals(String googleID);
    List<Book> findAllByBookclubsIs(BookclubMembership bookclubMembership);
}
