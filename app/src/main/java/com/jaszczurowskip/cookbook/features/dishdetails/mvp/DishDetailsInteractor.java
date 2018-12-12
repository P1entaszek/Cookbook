package com.jaszczurowskip.cookbook.features.dishdetails.mvp;

import android.support.annotation.NonNull;

import com.jaszczurowskip.cookbook.datasource.CookbookClient;
import com.jaszczurowskip.cookbook.datasource.ServerResponseListener;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;

/**
 * Created by jaszczurowskip on 07.12.2018
 */
public class DishDetailsInteractor implements DishDetailsMVP.Interactor {

    @Override
    public void getDishDetails(final @NonNull long dishID, final @NonNull DishDetailsInteractorCallback callback) {
        CookbookClient.getCookbookClient().getDish(dishID, new ServerResponseListener<DishesApiModel>() {
            @Override
            public void onSuccess(final @NonNull DishesApiModel response) {
                callback.onGetDishDetailsSuccessCallback(response);
            }

            @Override
            public void onError(final @NonNull ApiError error) {
                callback.onGetDishDetailsErrorCallback(error);
            }
        });
    }
}
