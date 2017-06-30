package com.example.waqarahmed.neighbourlinking.Classes;

import java.io.Serializable;

/**
 * Created by waqar on 6/23/2017.
 */


public class ServiceRequest implements Serializable{
    int id;
    String type, owner_house_address,owner_contact,owner_name,status,owner_image_url,powerMan_name,
            powerMan_image_url,powerMan_id,created_at,updated_at,cause,sender_id;

    public ServiceRequest(int id, String type, String owner_house_address, String owner_contact, String owner_name, String status, String owner_image_url, String powerMan_name, String powerMan_image_url, String powerMan_id, String created_at, String updated_at, String cause, String sender_id) {
        this.id = id;
        this.type = type;
        this.owner_house_address = owner_house_address;
        this.owner_contact = owner_contact;
        this.owner_name = owner_name;
        this.status = status;
        this.owner_image_url = owner_image_url;
        this.powerMan_name = powerMan_name;
        this.powerMan_image_url = powerMan_image_url;
        this.powerMan_id = powerMan_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.cause = cause;
        this.sender_id = sender_id;
    }

    public ServiceRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner_house_address() {
        return owner_house_address;
    }

    public void setOwner_house_address(String owner_house_address) {
        this.owner_house_address = owner_house_address;
    }

    public String getOwner_contact() {
        return owner_contact;
    }

    public void setOwner_contact(String owner_contact) {
        this.owner_contact = owner_contact;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner_image_url() {
        return owner_image_url;
    }

    public void setOwner_image_url(String owner_image_url) {
        this.owner_image_url = owner_image_url;
    }

    public String getPowerMan_name() {
        return powerMan_name;
    }

    public void setPowerMan_name(String powerMan_name) {
        this.powerMan_name = powerMan_name;
    }

    public String getPowerMan_image_url() {
        return powerMan_image_url;
    }

    public void setPowerMan_image_url(String powerMan_image_url) {
        this.powerMan_image_url = powerMan_image_url;
    }

    public String getPowerMan_id() {
        return powerMan_id;
    }

    public void setPowerMan_id(String powerMan_id) {
        this.powerMan_id = powerMan_id;
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

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
}
