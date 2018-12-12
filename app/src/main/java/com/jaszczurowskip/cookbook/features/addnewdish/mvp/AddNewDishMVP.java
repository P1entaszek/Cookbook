package com.jaszczurowskip.cookbook.features.addnewdish.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.jaszczurowskip.cookbook.datasource.CookbookClient;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishModelToPost;
import com.jaszczurowskip.cookbook.datasource.model.IngredientApiModel;

import java.util.List;

/**
 * Created by jaszczurowskip on 11.12.2018
 */
public interface AddNewDishMVP  {

    interface Interactor {

        void getAllIngredients(final @NonNull GetIngredientsFromService callback);

        void sendDishToserver(final @NonNull DishModelToPost dish, final @NonNull AddNewDishInteractorCallback callback);

        void sendIngredientToServer(final @NonNull String ingredient, final @NonNull AddNewIngredientInteractorCallback callback);

        interface AddNewIngredientInteractorCallback {
            void onAddIngredientSuccessCallback(final @NonNull String ingredient);

            void onAddIngredientErrorCallback(final @NonNull ApiError error);
        }

        interface AddNewDishInteractorCallback {
            void onAddDishSuccessCallback(final @NonNull DishModelToPost dish);

            void onAddDishErrorCallback(final @NonNull ApiError error);
        }

        interface GetIngredientsFromService {
            void onGetIngredientSuccessCallback(final @NonNull List<IngredientApiModel> ingredients);

            void onGetIngredientErrorCallback(final @NonNull ApiError error);
        }
    }

    interface Presenter extends Interactor.AddNewIngredientInteractorCallback,
            Interactor.AddNewDishInteractorCallback, Interactor.GetIngredientsFromService {

        void attach(final @NonNull View view);

        void destroy();

        void initView();

        void gotIngredientsFromService();

        boolean validateNewDishBeforePost(final @NonNull Context context, DishModelToPost dish);

        void sendDishToServer(final @NonNull DishModelToPost dish);

        void sendIngredientToServer(final @NonNull String ingredient);

        void getPicture(int reqCode, int resultCode, Intent data, Activity activity);
    }

    interface View {

        void prepareIngredientsRecycler();

        void showGeneralError(final @NonNull String error);

        void showAllIngredients(final @NonNull List<IngredientApiModel> ingredients);

        void setupAddNewDishListener();

        void addNewIngredientToDishFromList();

        void setupAddNewIngredientListener();

        void onAddedNewDish();

        void setupPhotoPickerListener();

        void showFormError(final @NonNull int errorCode);

        void setupSpinner();

        void setPicture(Bitmap selectedImage);
    }
}
