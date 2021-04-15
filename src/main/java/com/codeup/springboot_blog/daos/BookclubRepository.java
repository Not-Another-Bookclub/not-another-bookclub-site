package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.Bookclub;
import com.codeup.springboot_blog.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookclubRepository extends JpaRepository<Bookclub, Long> {

}
