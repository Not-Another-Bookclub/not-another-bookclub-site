package com.codeup.springboot_blog.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String googleID;

    @Column
    private String isbn;

    @OneToMany(mappedBy = "bookclub")
    @JsonManagedReference
    private List<BookclubBook> bookclubs;

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<UserBook> users;

    @OneToMany(mappedBy = "book")
    @JsonManagedReference
    private List<Post> posts;

    public Book() {};

    public Book(long id, String googleID, String isbn, List<BookclubBook> bookclubs, List<UserBook> users, List<Post> posts) {
        this.id = id;
        this.googleID = googleID;
        this.isbn = isbn;
        this.bookclubs = bookclubs;
        this.users = users;
        this.posts = posts;
    }

    public Book(String googleID) {
        this.googleID = googleID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogle_id(String googleID) {
        this.googleID = googleID;
    }

    public List<BookclubBook> getBookclubs() {
        return bookclubs;
    }

    public void setBookclubs(List<BookclubBook> bookclubs) {
        this.bookclubs = bookclubs;
    }

    public List<UserBook> getUsers() {
        return users;
    }

    public void setUsers(List<UserBook> users) {
        this.users = users;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
