package com.codeup.springboot_blog.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users_books")
public class UserBook implements Comparable<UserBook>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false,   referencedColumnName = "id")
    @JsonManagedReference
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "started")
    private Date started;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "finished")
    private Date finished;

    public UserBook () {};

    public int compareTo(UserBook userbook) {
        return userbook.started.compareTo(this.started);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Date getFinished() {
        return finished;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }
}
