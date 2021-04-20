package com.codeup.springboot_blog.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bookclubs")
public class Bookclub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private boolean is_private;

    @Column
    private boolean accepting_members;

    @OneToOne
    private User owner;

//    @ManyToMany(cascade =  CascadeType.ALL)
//    @JoinTable(
//            name = "bookclubs_users",
//            joinColumns = {@JoinColumn(name ="bookclub_id")},
//            inverseJoinColumns = {@JoinColumn(name = "user_id")}
//    )
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<BookclubMembership> users;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookclub")
    private List<Meeting> meetings;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
    private List<BookclubBook> books;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bookclub")
    private List<Post> posts;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "meeting")
//    private List<Meeting> meetings;

    public Bookclub() {};


    public Bookclub(String name, String description, boolean is_private, boolean accepting_members, User owner, List<BookclubMembership> users, List<Meeting> meetings) {
        this.name = name;
        this.description = description;
        this.is_private = is_private;
        this.accepting_members = accepting_members;
        this.owner = owner;
        this.users = users;
        this.meetings = meetings;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIs_private() {
        return is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    public boolean isAccepting_members() {
        return accepting_members;
    }

    public void setAccepting_members(boolean accepting_members) {
        this.accepting_members = accepting_members;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<BookclubMembership> getUsers() {
        return users;
    }

    public void setUsers(List<BookclubMembership> users) {
        this.users = users;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }
}
