package com.team4.uberapp.util;

/**
 * Created by lzhai on 2016/11/17.
 */
//@Data
public class AppUser {
    private String userType;
    private String userID;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public AppUser(String userType, String userID) {
        this.userID = userID;
        this.userType = userType;
    }
}
