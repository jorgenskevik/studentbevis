package com.example.jorgenskevik.e_cardholders.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by jorgenskevik on 11.07.2018.
 */

public class UnitMembership {

    @SerializedName("created_at")
    @Expose
    private Date created_at;

    @SerializedName("expiration_date")
    @Expose
    private Date expiration_date;

    @SerializedName("student_class")
    @Expose
    private String student_class;

    @SerializedName("student_number")
    @Expose
    private String student_number;

    @SerializedName("updated_at")
    @Expose
    private Date updated_at;

    @SerializedName("member")
    @Expose
    private String member;

    @SerializedName("member_unit")
    @Expose
    private int member_unit;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("membership_number")
    @Expose
    private String membership_number;

    @SerializedName("membership_type")
    @Expose
    private String membership_type;

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getStudent_number() {
        return student_number;
    }

    public void setStudent_number(String student_number) {
        this.student_number = student_number;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getMember_unit() {
        return member_unit;
    }

    public void setMember_unit(int member_unit) {
        this.member_unit = member_unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMembership_number() {
        return membership_number;
    }

    public void setMembership_number(String membership_number) {
        this.membership_number = membership_number;
    }

    public String getMembership_type() {
        return membership_type;
    }

    public void setMembership_type(String membership_type) {
        this.membership_type = membership_type;
    }
}
