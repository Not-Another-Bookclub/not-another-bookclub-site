package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
