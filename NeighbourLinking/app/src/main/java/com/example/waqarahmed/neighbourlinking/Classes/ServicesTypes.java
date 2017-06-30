package com.example.waqarahmed.neighbourlinking.Classes;

/**
 * Created by Waqar ahmed on 6/2/2017.
 */

public class ServicesTypes {
    int id;
    String Skill;
    String image_url;
    String created_at;
    String updated_at;

    public ServicesTypes() {
    }

    public ServicesTypes(int id, String skill, String image_url, String created_at, String updated_at) {
        this.id = id;
        Skill = skill;
        this.image_url = image_url;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSkill() {
        return Skill;
    }

    public void setSkill(String skill) {
        Skill = skill;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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
