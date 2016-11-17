package com.team4.uberapp.util;

import lombok.Data;

/**
 * Created by lzhai on 2016/11/17.
 */
@Data
public class AppUser {
    private String userType;
    private String userID;

    public AppUser(String userType, String userID) {
        this.userID = userID;
        this.userType = userType;
    }
}
