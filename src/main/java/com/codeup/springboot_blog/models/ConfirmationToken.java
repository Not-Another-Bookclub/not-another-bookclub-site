package com.codeup.springboot_blog.models;

import javax.persistence.*;

@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")
    private long tokenid;
//
//    @Column(name = "confirmation_token")
//    private String confirmationToken;
//
//    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
//    @JoinColumn(nullable = false, name = "id")
}
