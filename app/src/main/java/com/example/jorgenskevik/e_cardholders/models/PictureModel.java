package com.example.jorgenskevik.e_cardholders.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jorgenskevik on 30.03.2017.
 */

public class PictureModel {
    @SerializedName("pictureToken")
    @Expose
    private String pictureToken;

    @SerializedName("photo")
    @Expose
    private String photo;

    public PictureModel(String token, String photo){
        this.photo = photo;
        this.pictureToken = token;
    }
}
