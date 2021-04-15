package com.codeup.springboot_blog.models;

import com.mysql.cj.protocol.ColumnDefinition;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false, length = 255)
    private String title;

    @Column (columnDefinition = "TEXT", nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn (name = "author_id", referencedColumnName = "id", nullable = false)
    private User author;

    @OneToMany (cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn (name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn (name = "bookclub_id", referencedColumnName = "id", nullable = false)
    private Bookclub bookclub;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    public Post(){};



    public Post(long id, String title, String body, User author) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
    }

    public Post(String title, String body, User author) {
        this.title = title;
        this.body = body;
        this.author = author;
    }

    public Post(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Post(long id, String title, String body, User author, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.comments = comments;
    }

    public Post(long id, String title, String body, User author, List<Comment> comments, Book book, Bookclub bookclub, Date createDate, Date modifyDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.comments = comments;
        this.book = book;
        this.bookclub = bookclub;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Bookclub getBookclub() {
        return bookclub;
    }

    public void setBookclub(Bookclub bookclub) {
        this.bookclub = bookclub;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
