package com.codeup.springboot_blog.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users_books")
public class UserBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
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
}
