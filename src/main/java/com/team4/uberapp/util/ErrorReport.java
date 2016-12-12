package com.team4.uberapp.util;

import com.google.gson.Gson;

/**
 * Created by HectorGuo on 11/17/16.
 */
public class ErrorReport {
    private int errorCode;
    private String errorMsg;

    /**
     * Instantiates a new Error report.
     *
     * @param errorCode the error code
     * @param errorMsg  the error msg
     */
    public ErrorReport(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * Generate standard error report
     * @param errorCode error code
     * @param errorMsg error detail
     * @return string - json string
     */
    public static String toJson(int errorCode, String errorMsg) {
        Gson gson = new Gson();
        ErrorReport err = new ErrorReport(errorCode, errorMsg);
        return gson.toJson(err);
    }
}
