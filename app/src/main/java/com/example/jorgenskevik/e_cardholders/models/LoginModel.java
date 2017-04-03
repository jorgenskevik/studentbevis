package com.example.jorgenskevik.e_cardholders.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jorgenskevik on 23.03.2017.
 */

public class LoginModel {
    @SerializedName("token")
    @Expose
    public String token;

    @SerializedName("user")
    @Expose
    public User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
