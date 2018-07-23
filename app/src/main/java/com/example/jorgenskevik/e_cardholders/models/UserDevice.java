package com.example.jorgenskevik.e_cardholders.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by jorgenskevik on 11.07.2018.
 */

public class UserDevice {

    @SerializedName("created_at")
    @Expose
    private Date created_at;

    @SerializedName("device_description")
    @Expose
    private String device_description;

    @SerializedName("device_token")
    @Expose
    private String device_token;

    @SerializedName("device_user")
    @Expose
    private String device_user;

    @SerializedName("firebase_project_id")
    @Expose
    private String firebase_project_id;

    @SerializedName("updated_at")
    @Expose
    private Date updated_at;

    public UserDevice(String device_token){
        this.device_token = device_token;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getDevice_description() {
        return device_description;
    }

    public void setDevice_description(String device_description) {
        this.device_description = device_description;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_user() {
        return device_user;
    }

    public void setDevice_user(String device_user) {
        this.device_user = device_user;
    }

    public String getFirebase_project_id() {
        return firebase_project_id;
    }

    public void setFirebase_project_id(String firebase_project_id) {
        this.firebase_project_id = firebase_project_id;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
