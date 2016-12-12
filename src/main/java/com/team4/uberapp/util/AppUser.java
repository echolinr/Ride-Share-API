package com.team4.uberapp.util;

import lombok.Data;

/**
 * Created by lzhai on 2016/11/17.
 */
@Data
public class AppUser {
    private String userType;
    private String userID;

    /**
     * Instantiates a new App user.
     *
     * @param userID   the user id
     * @param userType the user type
     */
    public AppUser(String userID, String userType) {
        this.userID = userID;
        this.userType = userType;
    }
}
