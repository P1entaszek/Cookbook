package com.jaszczurowskip.cookbook.features.disheslist.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jaszczurowskip.cookbook.datasource.CookbookClient;
import com.jaszczurowskip.cookbook.datasource.ServerResponseListener;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;

import java.util.List;

/**
 * Created by jaszczurowskip on 10.12.2018
 */
public class DishesListInteractor implements DishesListMVP.Interactor {
    @Override
    public void getAllDishesList(final @NonNull DishesListInteractorCallback callback) {
        CookbookClient.getCookbookClient().getAllDishes(new ServerResponseListener<List<DishesApiModel>>() {
            @Override
            public void onSuccess(List<DishesApiModel> dishesList) {
                callback.onGetDishesListSuccesCallback(dishesList);
            }

            @Override
            public void onError(ApiError error) {
                callback.onGetDishesListErrorCallback(error);
            }
        });
    }

    @Override
    public void getAllSearchedDishes(@Nullable CharSequence query, @NonNull DishesListInteractorCallback callback) {
        CookbookClient.getCookbookClient().getSearchedDishes(query, new ServerResponseListener<List<DishesApiModel>>() {
            @Override
            public void onSuccess(List<DishesApiModel> dishesList) {
                callback.onGetDishesListSuccesCallback(dishesList);
            }

            @Override
            public void onError(ApiError error) {
                callback.onGetDishesListErrorCallback(error);
            }
        });
    }
}
