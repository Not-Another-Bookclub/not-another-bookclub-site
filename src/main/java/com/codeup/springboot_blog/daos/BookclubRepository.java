package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.Book;
import com.codeup.springboot_blog.models.Bookclub;
import com.codeup.springboot_blog.models.BookclubMembership;
import com.codeup.springboot_blog.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookclubRepository extends JpaRepository<Bookclub, Long> {
//    @Query("from Bookclub b where b.owner.id like id")
    List<Bookclub> findBookclubsByOwnerId(long id);
//    List<Bookclub> findBookclubsByIs_privateIsFalse();
//    List<Bookclub> findAllByIs_privateEquals(boolean booleanvalue);
    List<Bookclub> findAllByBooksIs(Book book);

//    ndBookclubsBy
//    List<BookclubMembership> fin
}
