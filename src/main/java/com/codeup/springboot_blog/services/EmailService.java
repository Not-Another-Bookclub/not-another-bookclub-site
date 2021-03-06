package com.codeup.springboot_blog.services;

import com.codeup.springboot_blog.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("mailService")
public class EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Value("${spring.mail.from}")
    private String from;

    public void prepareAndSend(String email, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(body);

        try{
            this.emailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }
//    public void prepareAndSend(String email, String subject, String body, String from) {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setFrom(from);
//        msg.setTo(email);
//        msg.setSubject(subject);
//        msg.setText(body);
//
//        try{
//            this.emailSender.send(msg);
//        }
//        catch (MailException ex) {
//            // simply log it and go on...
//            System.err.println(ex.getMessage());
//        }
//    }
}
