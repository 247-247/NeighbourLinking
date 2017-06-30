package com.example.waqarahmed.neighbourlinking.Classes;

/**
 * Created by waqas on 3/12/2017.
 */

public class Message {
    String recverId;
    String senderId;
    String name;
    String msg;
    public Message(){}
    public Message(String recverId, String senderId, String name, String msg) {
        this.recverId = recverId;
        this.senderId = senderId;
        this.name = name;
        this.msg = msg;
    }


    public String getRecverId() {
        return recverId;
    }

    public void setRecverId(String recverId) {
        this.recverId = recverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }




}
