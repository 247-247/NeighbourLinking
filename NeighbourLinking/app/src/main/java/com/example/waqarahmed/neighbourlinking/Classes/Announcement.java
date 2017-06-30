package com.example.waqarahmed.neighbourlinking.Classes;

/**
 * Created by Waqar ahmed on 5/23/2017.
 */

public class Announcement {
    int id;
    String email;
    String msg_body;
    String title;
    String sendind_to;
    String status;
    String created_at;
    String updated_at;

    public Announcement() {

    }

    public Announcement(int id, String email, String msg_body, String title, String sendind_to, String status, String created_at, String updated_at) {
        this.id = id;
        this.email = email;
        this.msg_body = msg_body;
        this.title = title;
        this.sendind_to = sendind_to;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsg_body() {
        return msg_body;
    }

    public void setMsg_body(String msg_body) {
        this.msg_body = msg_body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSendind_to() {
        return sendind_to;
    }

    public void setSendind_to(String sendind_to) {
        this.sendind_to = sendind_to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
