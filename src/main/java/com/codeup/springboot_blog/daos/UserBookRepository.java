package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.User;
import com.codeup.springboot_blog.models.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {
    List<UserBook> findAllByUser(User user);
}
