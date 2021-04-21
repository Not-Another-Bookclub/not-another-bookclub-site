package com.codeup.springboot_blog.daos;

import com.codeup.springboot_blog.models.Bookclub;
import com.codeup.springboot_blog.models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findAllByBookclubEquals(Bookclub bookclub);
    Meeting findMeetingByBookclubEqualsAndBook_GoogleID(Bookclub bookclub, String googleID);
}
