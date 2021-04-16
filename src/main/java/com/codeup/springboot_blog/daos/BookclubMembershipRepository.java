package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.Bookclub;
import com.codeup.springboot_blog.models.BookclubMembership;
import com.codeup.springboot_blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface BookclubMembershipRepository extends JpaRepository<BookclubMembership, Long> {
    BookclubMembership findByUserIs(User user);

    ArrayList<BookclubMembership> findBookclubMembershipsByUser(User user);
    ArrayList<BookclubMembership> findBookclubMembershipsByBookclub(Bookclub bookclub);
}
