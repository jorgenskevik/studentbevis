package com.example.jorgenskevik.e_cardholders.models;

import android.media.session.MediaSession;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * The type User.
 */
public class User {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("picture_token")
    @Expose
    private String picture_token;

    @SerializedName("picture")
    @Expose
    private String picture;

    @SerializedName("user_role")
    @Expose
    private int user_role;

    @SerializedName("date_of_birth")
    @Expose
    private Date date_of_birth;

    @SerializedName("has_logged_in")
    @Expose
    private boolean has_logged_in;


    @SerializedName("has_set_picture")
    @Expose
    private boolean has_set_picture;

    @SerializedName("updated_at")
    @Expose
    private Date updated_at;

    @SerializedName("created_at")
    @Expose
    private Date created_at;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture_token() {
        return picture_token;
    }

    public void setPicture_token(String picture_token) {
        this.picture_token = picture_token;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getUser_role() {
        return user_role;
    }

    public void setUser_role(int user_role) {
        this.user_role = user_role;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public boolean isHas_logged_in() {
        return has_logged_in;
    }

    public void setHas_logged_in(boolean has_logged_in) {
        this.has_logged_in = has_logged_in;
    }

    public boolean isHas_set_picture() {
        return has_set_picture;
    }

    public void setHas_set_picture(boolean has_set_picture) {
        this.has_set_picture = has_set_picture;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getFullName(){
        return this.first_name + " " + this.last_name;
    }
}

