package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.Bookclub;
import com.codeup.springboot_blog.models.BookclubBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookclubBookRepository extends JpaRepository <BookclubBook, Long> {
    List<BookclubBook> getAllByBookclub(Bookclub bookclub);
    BookclubBook getBookclubBookByBook_GoogleIDAndBookclub(String googleID, Bookclub bookclub);
}
