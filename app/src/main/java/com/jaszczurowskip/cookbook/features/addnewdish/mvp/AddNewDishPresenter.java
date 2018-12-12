package com.jaszczurowskip.cookbook.features.addnewdish.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishModelToPost;
import com.jaszczurowskip.cookbook.datasource.model.IngredientApiModel;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jaszczurowskip on 11.12.2018
 */
public class AddNewDishPresenter implements AddNewDishMVP.Presenter {

    private final AddNewDishMVP.Interactor interactor;
    private AddNewDishMVP.View view;

    public AddNewDishPresenter() {
        this.interactor = new AddNewDishInteractor();
    }

    @Override
    public void onAddIngredientSuccessCallback(final @NonNull String ingredient) {
        interactor.getAllIngredients(this);
    }

    @Override
    public void onAddIngredientErrorCallback(final @NonNull ApiError error) {
        view.showGeneralError(error.getMessage());
    }

    @Override
    public void onAddDishSuccessCallback(final @NonNull DishModelToPost dish) {
        view.onAddedNewDish();
    }

    @Override
    public void onAddDishErrorCallback(final @NonNull ApiError error) {
        view.showGeneralError(error.getMessage());
    }

    @Override
    public void onGetIngredientSuccessCallback(final @NonNull List<IngredientApiModel> ingredients) {
        view.showAllIngredients(ingredients);
    }

    @Override
    public void onGetIngredientErrorCallback(final @NonNull ApiError error) {
        view.showGeneralError(error.getMessage());
    }

    @Override
    public void attach(final @NonNull AddNewDishMVP.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void initView() {
        view.prepareIngredientsRecycler();
        view.setupSpinner();
        view.setupAddNewDishListener();
        view.addNewIngredientToDishFromList();
        view.setupPhotoPickerListener();
        view.setupAddNewIngredientListener();

    }

    @Override
    public void gotIngredientsFromService() {
        interactor.getAllIngredients(this);
    }

    @Override
    public boolean validateNewDishBeforePost(final @NonNull Context context, final @NonNull DishModelToPost dish) {
        boolean valid = true;
        String generalToast = context.getString(R.string.wrong_filled_dish_form);
        String newLine = context.getString(R.string.new_line);
        if (dish.getIngredientIds().isEmpty()) {
            generalToast = generalToast.concat(newLine).concat(context.getString(R.string.you_didnt_choose_dish_ingredients));
            valid = false;
        }
        if (dish.getName().isEmpty()) {
            generalToast = generalToast.concat(newLine).concat(context.getString(R.string.you_didnt_type_dish_name));
            view.showFormError(0);
            valid = false;
        }
        if (dish.getRecipe().isEmpty()) {
            generalToast = generalToast.concat(newLine).concat(context.getString(R.string.you_didnt_type_dish_description));
            view.showFormError(1);
            valid = false;
        }
        if (dish.getPicture() == null) {
            generalToast = generalToast.concat(newLine).concat(context.getString(R.string.you_didnt_choose_dish_picture));
            valid = false;
        }
        if (!valid) view.showGeneralError(generalToast);
        return valid;
    }


    @Override
    public void sendDishToServer(final @NonNull DishModelToPost dish) {
        interactor.sendDishToserver(dish, this);
    }


    @Override
    public void sendIngredientToServer(final @NonNull String ingredient) {
        interactor.sendIngredientToServer(ingredient, this);
    }

    @Override
    public void getPicture(int reqCode, int resultCode, Intent data, Activity activity) {
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = activity.getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                view.setPicture(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                view.showFormError(2);
            }

        } else {
            view.showFormError(3);
        }
    }
}
