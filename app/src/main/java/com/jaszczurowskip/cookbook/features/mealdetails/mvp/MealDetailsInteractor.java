package com.jaszczurowskip.cookbook.features.mealdetails.mvp;

import com.jaszczurowskip.cookbook.datasource.CookbookClient;
import com.jaszczurowskip.cookbook.datasource.ServerResponseListener;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;

/**
 * Created by jaszczurowskip on 07.12.2018
 */
public class MealDetailsInteractor implements MealDetailsMVP.Interactor {

    @Override
    public void getMealDetails(long dishID, final MealsDetailsInteractorCallback callback) {
        CookbookClient.getCookbookClient().getDish(dishID, new ServerResponseListener<DishesApiModel>() {
            @Override
            public void onSuccess(DishesApiModel response) {
                callback.onSuccess(response);
            }

            @Override
            public void onError(ApiError error) {
                callback.onError(error);
            }
        });
    }
}
