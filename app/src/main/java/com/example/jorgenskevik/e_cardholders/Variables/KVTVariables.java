package com.example.jorgenskevik.e_cardholders.Variables;

import android.content.SharedPreferences;

/**
 * Created by jorgenskevik on 23.03.2017.
 */

public class KVTVariables {

    private static String Local_URL = "http://10.0.2.2:9000/";
    private static String TWITTER_KEY = "OwT8vAGt92TMSDRK3O9naZznW";
    private static String TWITTER_SECRET = "PfC1rhbLQ8WSnmJCgqKwyfPZJyjyAoLHzx61sClci42tiiA38R";

    private static String Appkey = "SF6y36FZ698y7oqUCbB6uvr0o2yaHW2H";
    private static String AcceptVersion = "V1";
    private static String BASE_URL = "http://ec2-34-249-6-7.eu-west-1.compute.amazonaws.com:6900/";
    private static String tokenlogin = "";
    private static String userID = "";
    private static String userName = "";
    private static String userEmail = "";
    private static String tokenPhone = "";
    private static String pictureToken = "";
    private static String picture = "";
    private static String role = "";
    private static String birthday = "";
    private static SharedPreferences userIdPrefs;


    public static String getLocal_URL() {
        return Local_URL;
    }

    public static String getTwitterKey() {
        return TWITTER_KEY;
    }

    public static String getTwitterSecret() {
        return TWITTER_SECRET;
    }

    public static String getAppkey() {
        return Appkey;
    }

    public static String getAcceptVersion() {
        return AcceptVersion;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getTokenlogin() {
        return tokenlogin;
    }

    public static String getUserID() {
        return userID;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static String getTokenPhone() {
        return tokenPhone;
    }

    public static String getPictureToken() {
        return pictureToken;
    }

    public static String getPicture() {
        return picture;
    }

    public static String getRole() {
        return role;
    }

    public static void setLocal_URL(String local_URL) {
        Local_URL = local_URL;
    }

    public static void setTwitterKey(String twitterKey) {
        TWITTER_KEY = twitterKey;
    }

    public static void setTwitterSecret(String twitterSecret) {
        TWITTER_SECRET = twitterSecret;
    }

    public static void setAppkey(String appkey) {
        Appkey = appkey;
    }

    public static void setAcceptVersion(String acceptVersion) {
        AcceptVersion = acceptVersion;
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static void setTokenlogin(String tokenlogin) {
        KVTVariables.tokenlogin = tokenlogin;
    }

    public static void setUserID(String userID) {
        KVTVariables.userID = userID;
    }

    public static void setUserName(String userName) {
        KVTVariables.userName = userName;
    }

    public static void setUserEmail(String userEmail) {
        KVTVariables.userEmail = userEmail;
    }

    public static void setTokenPhone(String tokenPhone) {
        KVTVariables.tokenPhone = tokenPhone;
    }

    public static void setPictureToken(String pictureToken) {
        KVTVariables.pictureToken = pictureToken;
    }

    public static void setPicture(String picture) {
        KVTVariables.picture = picture;
    }

    public static void setRole(String role) {
        KVTVariables.role = role;
    }

    public static SharedPreferences getUserIdPrefs() {
        return userIdPrefs;
    }

    public static void setUserIdPrefs(SharedPreferences userIdPrefs) {
        KVTVariables.userIdPrefs = userIdPrefs;
    }

    public static String getBirthday() {
        return birthday;
    }

    public static void setBirthday(String birthday) {
        KVTVariables.birthday = birthday;
    }
}
