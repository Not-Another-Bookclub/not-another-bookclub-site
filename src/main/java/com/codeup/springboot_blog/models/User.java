package com.codeup.springboot_blog.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    @JsonIgnore
    private String password;

    @Column (columnDefinition = "TEXT")
    private String bio;

    @Column (columnDefinition = "VARCHAR(255) default '/img/user-solid.svg'")
    private String avatar_path;

    @Column
    private boolean is_admin;

    @Column
    private boolean is_private;

    @Column
    private String zipcode;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "id")
    @JsonBackReference
    private List<Post> posts;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "comment_id")
    @JsonBackReference
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookclub")
    @JsonBackReference
    private List<BookclubMembership> bookclubMemberships;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    @JsonBackReference
    private List<UserBook> books;

    public User(long id, String username, String firstname, String lastname, String email, String password, String bio, String avatar_path, boolean is_admin, boolean is_private, List<Post> posts, List<Comment> comments) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.avatar_path = avatar_path;
        this.is_admin = is_admin;
        this.is_private = is_private;
        this.posts = posts;
        this.comments = comments;
    }

    public User(User copy) {
        id = copy.id;
        email = copy.email;
        username = copy.username;

        password = copy.password;
        bio = copy.bio;
        avatar_path = copy.avatar_path;
    }

    public User() {};
    public User(long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(long id, String username, String email, String password, List<Post> posts) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.posts = posts;
    }

    public User(long id, String username, String email, String password, String bio, String avatar_path, List<Post> posts) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.avatar_path = avatar_path;
        this.posts = posts;
    }

    public User(long id, String username, String email, String password, String bio, String avatar_path, List<Post> posts, List<Comment> comments) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.avatar_path = avatar_path;
        this.posts = posts;
        this.comments = comments;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public boolean isIs_private() {
        return is_private;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public List<BookclubMembership> getBookclubs() {
        return bookclubMemberships;
    }

    public void setBookclubMemberships(List<BookclubMembership> bookclubMemberships) {
        this.bookclubMemberships = bookclubMemberships;
    }

    public List<UserBook> getBooks() {
        return books;
    }

    public void setBooks(List<UserBook> books) {
        this.books = books;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
