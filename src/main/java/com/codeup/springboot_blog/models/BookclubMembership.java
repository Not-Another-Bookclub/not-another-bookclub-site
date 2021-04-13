package com.codeup.springboot_blog.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookclubs_users")
public class BookclubMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "bookclub_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private Bookclub bookclub;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookclubMembershipStatus status;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_changed")
    private Date changedDate;

    @ManyToOne
    @JoinColumn(name = "last_changed_by")
    @JsonManagedReference
    private User lastChangedBy;

    public BookclubMembership() {};

    public BookclubMembership(long id, Bookclub bookclub, User user, BookclubMembershipStatus status, Date changedDate, User lastChangedBy) {
        this.id = id;
        this.bookclub = bookclub;
        this.user = user;
        this.status = status;
        this.changedDate = changedDate;
        this.lastChangedBy = lastChangedBy;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookclubMembershipStatus getStatus() {
        return status;
    }

    public void setStatus(BookclubMembershipStatus status) {
        this.status = status;
    }

    public Date getChangedDate() {
        return changedDate;
    }

    public void setChangedDate(Date changedDate) {
        this.changedDate = changedDate;
    }

    public User getLastChangedBy() {
        return lastChangedBy;
    }

    public void setLastChangedBy(User lastChangedBy) {
        this.lastChangedBy = lastChangedBy;
    }
}
