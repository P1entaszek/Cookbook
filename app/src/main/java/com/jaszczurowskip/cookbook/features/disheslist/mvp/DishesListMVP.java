package com.jaszczurowskip.cookbook.features.disheslist.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;

import java.util.List;

/**
 * Created by jaszczurowskip on 10.12.2018
 */
public interface DishesListMVP {

    interface Interactor {
        void getAllDishesList(final @NonNull DishesListInteractorCallback callback);

        void getAllSearchedDishes(final @Nullable CharSequence query, final @NonNull DishesListInteractorCallback callback);

        interface DishesListInteractorCallback {
            void onSuccesCallback(final @NonNull List<DishesApiModel> dishesList);

            void onErrorCallback(final @NonNull ApiError apiError);
        }
    }

    interface Presenter extends Interactor.DishesListInteractorCallback{
        void attach(final @NonNull View view);

        void gotDishesListFromService();

        void gotSearchedDishesListFromService(final @NonNull String query);

        void destroy();

        void initView();

    }

    interface View {

        void showProgressDialog();

        void dismissProgressDialog();

        void showError(final @NonNull String error);

        void showDishesList(final @NonNull List<DishesApiModel> dishesList);

        void setupSearchDishesListener();

        void setupAddNewMealListener();
    }
}
