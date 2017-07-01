package com.example.waqarahmed.neighbourlinking.Classes;

/**
 * Created by waqar on 7/1/2017.
 */

public class Brand {

int id;
    String name,image_url,address,fcm_id,status,contact,currentStatus,email,created_at,updated_at,password,isAccountSetUp;

    public Brand() {
    }

    public Brand(int id, String name, String image_url, String address, String fcm_id, String status, String contact, String currentStatus, String email,
                 String created_at, String updated_at, String password, String isAccountSetUp) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.address = address;
        this.fcm_id = fcm_id;
        this.status = status;
        this.contact = contact;
        this.currentStatus = currentStatus;
        this.email = email;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.password = password;
        this.isAccountSetUp = isAccountSetUp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsAccountSetUp() {
        return isAccountSetUp;
    }

    public void setIsAccountSetUp(String isAccountSetUp) {
        this.isAccountSetUp = isAccountSetUp;
    }
}
