package com.jaszczurowskip.cookbook.datasource.model;

import android.support.annotation.NonNull;

/**
 * Created by Jakub Mateusiak on 2016-06-29.
 */

public class ApiError {
    private long timestamp;
    private int status;
    @NonNull
    private String error;
    @NonNull
    private String exception;
    @NonNull
    private String message;
    @NonNull
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

    @NonNull
    public String getError() {
        return error;
    }

    @NonNull
    public String getException() {
        return exception;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @NonNull
    public String getPath() {
        return path;
    }
}
