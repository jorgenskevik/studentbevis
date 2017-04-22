package com.example.jorgenskevik.e_cardholders.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The type Login model.
 */
public class LoginModel {
    /**
     * The Token.
     */
    @SerializedName("token")
    @Expose
    public String token;

    /**
     * The User.
     */
    @SerializedName("user")
    @Expose
    public User user;

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets token.
     *
     * @param token the token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
