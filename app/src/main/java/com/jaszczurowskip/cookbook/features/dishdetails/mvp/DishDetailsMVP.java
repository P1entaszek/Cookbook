package com.jaszczurowskip.cookbook.features.dishdetails.mvp;

import android.support.annotation.NonNull;

import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;

/**
 * Created by jaszczurowskip on 07.12.2018
 */
public interface DishDetailsMVP {

    interface Interactor {
        void getDishDetails(final @NonNull long dishID, final @NonNull DishDetailsInteractorCallback callback);

        interface DishDetailsInteractorCallback {
            void onSuccessCallback(final @NonNull DishesApiModel dish);

            void onErrorCallback(final @NonNull ApiError error);
        }
    }


    interface Presenter extends Interactor.DishDetailsInteractorCallback {
        void attach(final @NonNull View view);

        void gotDishFromService(final @NonNull long dishId);

        void destroy();

        void initView();
    }

    interface View {
        void setupview();

        void showProgressDialog();

        void dismissProgressDialog();

        void showError( final @NonNull String error);

        void displayDish(final @NonNull DishesApiModel dish);
    }
}
