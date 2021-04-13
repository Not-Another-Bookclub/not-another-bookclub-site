package com.codeup.springboot_blog.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long isbn;

    @OneToMany(mappedBy = "bookclub")
    private List<BookclubBook> bookclubs;

    @OneToMany(mappedBy = "book")
    private List<UserBook> users;

    public Book() {};

    public Book(long isbn) {
        this.isbn = isbn;
    }
}
