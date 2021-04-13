package com.codeup.springboot_blog.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "when")
    private Date when;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Where where;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String zipPlusFour;

    @ManyToOne
    @JoinColumn (name = "bookclub_id", referencedColumnName = "id", nullable = false)
    private Bookclub bookclub;

    public Meeting() {};

    public Meeting(Date createDate, Where where, String address, String city, String zipPlusFour, Bookclub bookclub) {
        this.when = createDate;
        this.where = where;
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

    public Date getCreateDate() {
        return when;
    }

    public void setCreateDate(Date createDate) {
        this.when = createDate;
    }

    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
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
}
