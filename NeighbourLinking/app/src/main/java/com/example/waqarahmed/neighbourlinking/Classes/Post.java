package com.example.waqarahmed.neighbourlinking.Classes;

/**
 * Created by waqas on 2/19/2017.
 */

public class Post {
    String title;
    String desc;
     String uid;
    String username;
    String sender_image;
     String send_date;

    public Post() {

    }



    public Post(String uid ,String title, String desc,  String username, String sender_image, String send_date) {
        this.title = title;
        this.desc = desc;
       this.uid = uid;
        this.username = username;
        this.sender_image = sender_image;
        this.send_date = send_date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
