package com.jaszczurowskip.cookbook.datasource;

import android.support.annotation.Nullable;

import com.jaszczurowskip.cookbook.datasource.model.ApiError;

/**
 * Created by jaszczurowskip on 03.12.2018
 */
public interface ServerResponseListener<T> {

    void onSuccess(final @Nullable T response);

    void onError(final @Nullable ApiError error);
}
