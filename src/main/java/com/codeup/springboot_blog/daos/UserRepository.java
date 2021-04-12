package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
