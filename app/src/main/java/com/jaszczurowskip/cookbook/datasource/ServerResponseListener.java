package com.jaszczurowskip.cookbook.datasource;

import com.jaszczurowskip.cookbook.datasource.model.ApiError;

/**
 * Created by jaszczurowskip on 03.12.2018
 */
public interface ServerResponseListener<T> {

    void onSuccess(T response);

    void onError(ApiError error);
}
