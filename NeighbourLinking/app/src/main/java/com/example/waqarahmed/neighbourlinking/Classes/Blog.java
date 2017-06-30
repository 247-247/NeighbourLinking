package com.example.waqarahmed.neighbourlinking.Classes;

/**
 * Created by waqas on 2/19/2017.
 */

public class Blog {
    String title;
    String desc;
    String post_image;
    String username;
    String sender_image;
     String send_date;

    public Blog() {

    }

    public Blog(String title, String desc, String post_image, String username, String sender_image, String send_date) {
        this.title = title;
        this.desc = desc;
        this.post_image = post_image;
        this.username = username;
        this.sender_image = sender_image;
        this.send_date = send_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSender_image() {
        return sender_image;
    }

    public void setSender_image(String sender_image) {
        this.sender_image = sender_image;
    }

    public String getSend_date() {
        return send_date;
    }

    public void setSend_date(String send_date) {
        this.send_date = send_date;
    }
}
