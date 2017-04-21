package com.example.jorgenskevik.e_cardholders.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jorgenskevik on 30.03.2017.
 */
public class PictureModel {
    //hei
    @SerializedName("pictureToken")
    @Expose
    private String pictureToken;

    @SerializedName("photo")
    @Expose
    private String photo;

    /**
     * Instantiates a new Picture model.
     *
     * @param token the token
     * @param photo the photo
     */
    public PictureModel(String token, String photo){
        this.photo = photo;
        this.pictureToken = token;
    }
}
