package com.jaszczurowskip.cookbook.features.mealdetails.mvp;

import com.jaszczurowskip.cookbook.datasource.CookbookClient;
import com.jaszczurowskip.cookbook.datasource.ServerResponseListener;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;

/**
 * Created by jaszczurowskip on 07.12.2018
 */
public class MealDetailsPresenter implements MealDetailsMVP.Presenter {

    private MealDetailsMVP.View mView;

    public MealDetailsPresenter() {
    }


    @Override
    public void onSuccess(DishesApiModel dish) {

    }

    @Override
    public void onError(ApiError error) {

    }

    @Override
    public void attach(MealDetailsMVP.View view) {
        mView = view;
    }

    @Override
    public void destroy() {
        mView = null;
    }

    @Override
    public void initView() {
        mView.showProgressDialog();
        mView.setupview();
    }

    @Override
    public void getMealsListFromService(long dishId) {
        CookbookClient.getCookbookClient().getDish(dishId, new ServerResponseListener<DishesApiModel>() {
            @Override
            public void onSuccess(DishesApiModel dish) {
                mView.dismissProgressDialog();
                mView.displayList(dish);
            }

            @Override
            public void onError(ApiError error) {
                mView.dismissProgressDialog();
                mView.showError(error.getMessage());
            }
        });
    }
}
