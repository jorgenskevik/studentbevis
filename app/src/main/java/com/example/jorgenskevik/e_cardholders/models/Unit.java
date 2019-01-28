package com.example.jorgenskevik.e_cardholders.models;

import com.digits.sdk.android.models.Email;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by jorgenskevik on 11.07.2018.
 */

public class Unit {

    @SerializedName("createdAt")
    @Expose
    private Date createdAt;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("organisation_number")
    @Expose
    private String organisation_number;

    @SerializedName("private_contact_email")
    @Expose
    private String private_contact_email;

    @SerializedName("private_contact_name")
    @Expose
    private String private_contact_name;

    @SerializedName("public_contact_email")
    @Expose
    private String public_contact_email;

    @SerializedName("public_contact_phone")
    @Expose
    private String public_contact_phone;

    @SerializedName("short_name")
    @Expose
    private String short_name;

    @SerializedName("unit_logo")
    @Expose
    private String unit_logo;

    @SerializedName("updated_at")
    @Expose
    private Date updated_at;

    @SerializedName("created_at")
    @Expose
    private Date created_at;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("card_type")
    @Expose
    private String card_type;

    @SerializedName("authentication_type")
    @Expose
    private String authentication_type;

    @SerializedName("small_unit_logo")
    @Expose
    private String small_unit_logo;

    @SerializedName("tag")
    @Expose
    private String tag;

    public Unit(String unit_name, String unit_logo, String tag, int id, String short_name){
        this.name = unit_name;
        this.unit_logo = unit_logo;
        this.tag = tag;
        this.id = id;
        this.short_name = short_name;
    }

    public String getSmall_unit_logo() {
        return small_unit_logo;
    }

    public void setSmall_unit_logo(String small_unit_logo) {
        this.small_unit_logo = small_unit_logo;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getAuthentication_type() {
        return authentication_type;
    }

    public void setAuthentication_type(String authentication_type) {
        this.authentication_type = authentication_type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganisation_number() {
        return organisation_number;
    }

    public void setOrganisation_number(String organisation_number) {
        this.organisation_number = organisation_number;
    }

    public String getPrivate_contact_email() {
        return private_contact_email;
    }

    public void setPrivate_contact_email(String private_contact_email) {
        this.private_contact_email = private_contact_email;
    }

    public String getPrivate_contact_name() {
        return private_contact_name;
    }

    public void setPrivate_contact_name(String private_contact_name) {
        this.private_contact_name = private_contact_name;
    }

    public String getPublic_contact_email() {
        return public_contact_email;
    }

    public void setPublic_contact_email(String public_contact_email) {
        this.public_contact_email = public_contact_email;
    }

    public String getPublic_contact_phone() {
        return public_contact_phone;
    }

    public void setPublic_contact_phone(String public_contact_phone) {
        this.public_contact_phone = public_contact_phone;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getUnit_logo() {
        return unit_logo;
    }

    public void setUnit_logo(String unit_logo) {
        this.unit_logo = unit_logo;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
