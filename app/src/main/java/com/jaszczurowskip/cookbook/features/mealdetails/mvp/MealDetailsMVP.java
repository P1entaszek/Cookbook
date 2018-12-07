package com.jaszczurowskip.cookbook.features.mealdetails.mvp;

import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;

/**
 * Created by jaszczurowskip on 07.12.2018
 */
public interface MealDetailsMVP {

    interface Interactor {
        void getMealDetails(long dishID, MealsDetailsInteractorCallback callback);

        interface MealsDetailsInteractorCallback {
            void onSuccess(DishesApiModel dish);

            void onError(ApiError error);
        }
    }


    interface Presenter extends Interactor.MealsDetailsInteractorCallback {
        void attach(View view);

        void getMealsListFromService(long dishId);

        void destroy();

        void initView();
    }

    interface View {
        void setupview();

        void showProgressDialog();

        void dismissProgressDialog();

        void showError(String error);

        void displayList(DishesApiModel dish);
    }
}
