package com.jaszczurowskip.cookbook.datasource.model;

import android.support.annotation.NonNull;

/**
 * Created by Jakub Mateusiak on 2016-06-29.
 */

public class ApiError {

    private long timestamp;
    private int status;
    private String error;
    private String exception;
    private String message;
    private String path;

    public static ApiError getServerConnectionError(final @NonNull String msg) {
        ApiError error = new ApiError();
        error.status = 500;
        error.message = msg;
        return error;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}
