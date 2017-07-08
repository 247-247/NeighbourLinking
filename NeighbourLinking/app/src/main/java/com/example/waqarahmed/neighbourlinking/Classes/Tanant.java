package com.example.waqarahmed.neighbourlinking.Classes;

/**
 * Created by waqar on 7/8/2017.
 */

public class Tanant {
    String first_name,last_name,mobl,city,uid,create_date,image,gender,Email;

    public Tanant() {

    }

    public Tanant(String first_name, String last_name, String mobl, String city, String uid, String create_date, String image, String gender, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.mobl = mobl;
        this.city = city;
        this.uid = uid;
        this.create_date = create_date;
        this.image = image;
        this.gender = gender;
        Email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMobl() {
        return mobl;
    }

    public void setMobl(String mobl) {
        this.mobl = mobl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
