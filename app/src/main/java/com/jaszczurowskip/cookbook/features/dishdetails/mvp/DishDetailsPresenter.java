package com.jaszczurowskip.cookbook.features.dishdetails.mvp;

import android.support.annotation.NonNull;

import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;

/**
 * Created by jaszczurowskip on 07.12.2018
 */
public class DishDetailsPresenter implements DishDetailsMVP.Presenter{

    private DishDetailsMVP.View view;
    private final DishDetailsMVP.Interactor interactor;

    public DishDetailsPresenter() {
        this.interactor = new DishDetailsInteractor();
    }

    @Override
    public void attach(final @NonNull DishDetailsMVP.View view) {
        this.view = view;
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void initView() {
        view.showProgressDialog();
        view.setupview();
    }

    @Override
    public void gotDishFromService(final @NonNull long dishId) {
        interactor.getDishDetails(dishId, this);
    }

    @Override
    public void onGetDishDetailsSuccessCallback(final @NonNull DishesApiModel dish) {
        view.dismissProgressDialog();
        view.displayDish(dish);
    }

    @Override
    public void onGetDishDetailsErrorCallback(final @NonNull ApiError apiError) {
        view.dismissProgressDialog();
        view.showError(apiError.getMessage());
    }
}
