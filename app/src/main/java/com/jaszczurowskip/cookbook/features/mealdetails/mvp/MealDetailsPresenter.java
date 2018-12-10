package com.jaszczurowskip.cookbook.features.mealdetails.mvp;

import com.jaszczurowskip.cookbook.datasource.CookbookClient;
import com.jaszczurowskip.cookbook.datasource.ServerResponseListener;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;

/**
 * Created by jaszczurowskip on 07.12.2018
 */
public class MealDetailsPresenter implements MealDetailsMVP.Presenter, MealDetailsMVP.Interactor.MealsDetailsInteractorCallback {

    private MealDetailsMVP.View mView;
    private MealDetailsMVP.Interactor interactor;

    public MealDetailsPresenter() {
        this.interactor = new MealDetailsInteractor();
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
    public void gotMealsListFromService(long dishId) {
        interactor.getMealDetails(dishId, this);
    }

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
}
