package com.example.jorgenskevik.e_cardholders.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UnitLinks {

    @SerializedName("createdAt")
    @Expose
    private Date createdAt;

    @SerializedName("updated_at")
    @Expose
    private Date updated_at;

    @SerializedName("unit")
    @Expose
    private int unit;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("name")
    @Expose
    private String name;

    public UnitLinks(Date createdAt, Date updated_at, int unit, String link, String name) {
        this.createdAt = createdAt;
        this.updated_at = updated_at;
        this.unit = unit;
        this.link = link;
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
