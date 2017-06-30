package com.example.waqarahmed.neighbourlinking.Classes;

/**
 * Created by Waqar ahmed on 5/13/2017.
 */

public class Contact {
    String address;
    String city;
    String create_date;
    String first_name;
    String image;
    String last_name;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Contact() {

    }

    public Contact(String address, String city, String create_date, String first_name, String image, String last_name ,String uid) {
        this.address = address;
        this.city = city;
        this.create_date = create_date;
        this.first_name = first_name;
        this.image = image;
        this.last_name = last_name;
        this.uid = uid;
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

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }





}
