package com.codeup.springboot_blog.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timedate")
    private Date timedate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Location location;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String zipPlusFour;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn (name = "bookclub_id", referencedColumnName = "id", nullable = false)
    private Bookclub bookclub;

    @OneToOne
    private Book book;

    public Meeting() {};

    public Meeting(Date createDate, Date when, String address, String city, String zipPlusFour, Bookclub bookclub) {
        this.timedate = createDate;
        this.location = location;
        this.address = address;
        this.city = city;
        this.zipPlusFour = zipPlusFour;
        this.bookclub = bookclub;
    }

    public Meeting(long id, Date timedate, Location location, String address, String city, String zipPlusFour, Bookclub bookclub) {
        this.id = id;
        this.timedate = timedate;
        this.location = location;
        this.address = address;
        this.city = city;
        this.zipPlusFour = zipPlusFour;
        this.bookclub = bookclub;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTimedate() {
        return timedate;
    }

    public void setTimedate(Date timedate) {
        this.timedate = timedate;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipPlusFour() {
        return zipPlusFour;
    }

    public void setZipPlusFour(String zipPlusFour) {
        this.zipPlusFour = zipPlusFour;
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
}
