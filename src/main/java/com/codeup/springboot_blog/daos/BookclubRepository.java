package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.Bookclub;
import com.codeup.springboot_blog.models.BookclubMembership;
import com.codeup.springboot_blog.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookclubRepository extends JpaRepository<Bookclub, Long> {
//    @Query("from Bookclub b where b.owner.id like id")
    List<Bookclub> findBookclubsByOwnerId(@Param("id") long id);

//    ndBookclubsBy
//    List<BookclubMembership> fin
}
