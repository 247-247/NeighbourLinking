package com.example.waqarahmed.neighbourlinking.Classes;

/**
 * Created by Waqar ahmed on 5/13/2017.
 */

public class Comment {


  String   comment_sender_name;
    String comment_sender_image;
    String comment_body;
    String sending_date ;
    String   sender_uid;

    public Comment() {

    }

    public Comment(String comment_sender_name, String comment_sender_image, String comment_body, String sending_date, String sender_uid) {
        this.comment_sender_name = comment_sender_name;
        this.comment_sender_image = comment_sender_image;
        this.comment_body = comment_body;
        this.sending_date = sending_date;
        this.sender_uid = sender_uid;
    }

    public String getComment_sender_name() {
        return comment_sender_name;
    }

    public void setComment_sender_name(String comment_sender_name) {
        this.comment_sender_name = comment_sender_name;
    }

    public String getComment_sender_image() {
        return comment_sender_image;
    }

    public void setComment_sender_image(String comment_sender_image) {
        this.comment_sender_image = comment_sender_image;
    }

    public String getComment_body() {
        return comment_body;
    }

    public void setComment_body(String comment_body) {
        this.comment_body = comment_body;
    }

    public String getSending_date() {
        return sending_date;
    }

    public void setSending_date(String sending_date) {
        this.sending_date = sending_date;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }





}
