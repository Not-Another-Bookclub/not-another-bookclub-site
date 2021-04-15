package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.BookclubBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookclubBookRepository extends JpaRepository <BookclubBook, Long> {
}
