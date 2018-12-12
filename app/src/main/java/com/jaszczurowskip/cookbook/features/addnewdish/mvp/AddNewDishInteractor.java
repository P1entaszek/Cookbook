package com.jaszczurowskip.cookbook.features.addnewdish.mvp;

import android.support.annotation.NonNull;

import com.jaszczurowskip.cookbook.datasource.CookbookClient;
import com.jaszczurowskip.cookbook.datasource.ServerResponseListener;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishModelToPost;
import com.jaszczurowskip.cookbook.datasource.model.IngredientApiModel;

import java.util.List;

/**
 * Created by jaszczurowskip on 11.12.2018
 */
public class AddNewDishInteractor implements AddNewDishMVP.Interactor {
    @Override
    public void getAllIngredients(@NonNull GetIngredientsFromService callback) {
        CookbookClient.getCookbookClient().getAllIngredients(new ServerResponseListener<List<IngredientApiModel>>() {
            @Override
            public void onSuccess(List<IngredientApiModel> ingredients) {
                callback.onGetIngredientSuccessCallback(ingredients);
            }

            @Override
            public void onError(ApiError error) {
                callback.onGetIngredientErrorCallback(error);
            }
        });
    }

    @Override
    public void sendDishToserver(@NonNull DishModelToPost dish, @NonNull AddNewDishInteractorCallback callback) {
        CookbookClient.getCookbookClient().sendDishToServer(dish, new ServerResponseListener<DishModelToPost>() {
            @Override
            public void onSuccess(DishModelToPost dish) {
                callback.onAddDishSuccessCallback(dish);
            }

            @Override
            public void onError(ApiError error) {
                callback.onAddDishErrorCallback(error);
            }
        });
    }

    @Override
    public void sendIngredientToServer(@NonNull String ingredient, @NonNull AddNewIngredientInteractorCallback callback) {
        CookbookClient.getCookbookClient().sendIngredientToServer(ingredient, new ServerResponseListener<String>() {
            @Override
            public void onSuccess(String ingredient) {
                callback.onAddIngredientSuccessCallback(ingredient);
            }

            @Override
            public void onError(ApiError error) {
                callback.onAddIngredientErrorCallback(error);
            }
        });
    }
}
