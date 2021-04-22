package com.codeup.springboot_blog.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookclubs_books")
public class BookclubBook implements Comparable<BookclubBook>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "bookclub_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private Bookclub bookclub;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private Book book;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    public BookclubBook() {};

    public BookclubBook(long id, Bookclub bookclub, Book book) {
        this.id = id;
        this.bookclub = bookclub;
        this.book = book;
    }

    public BookclubBook(Bookclub bookclub, Book book, Date startDate) {
        this.bookclub = bookclub;
        this.book = book;
        this.startDate = startDate;
    }

    public int compareTo(BookclubBook bookclubBook) {
        return bookclubBook.startDate.compareTo(this.startDate);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bookclub getBookclub() {
        return bookclub;
    }

    public void setBookclub(Bookclub bookclub) {
        this.bookclub = bookclub;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
