package com.example.waqarahmed.neighbourlinking.Classes;

/**
 * Created by Waqar ahmed on 6/19/2017.
 */

public class ServiceMan {

    int id;
   String  email,name,contact,skill,status,image_url,isAccountSetUp;

    public ServiceMan() {
    }

    public ServiceMan(int id, String email, String name, String contact, String skill, String status, String image_url,String isAccountSetUp) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.contact = contact;
        this.skill = skill;
        this.status = status;
        this.image_url = image_url;
        this.isAccountSetUp = isAccountSetUp;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    public String getIsAccountSetUp() {
        return isAccountSetUp;
    }

    public void setIsAccountSetUp(String isAccountSetUp) {
        this.isAccountSetUp = isAccountSetUp;
    }

}
