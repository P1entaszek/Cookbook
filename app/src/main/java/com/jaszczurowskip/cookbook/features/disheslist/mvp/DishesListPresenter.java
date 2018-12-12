package com.jaszczurowskip.cookbook.features.disheslist.mvp;

import android.support.annotation.NonNull;

import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;

import java.util.List;

/**
 * Created by jaszczurowskip on 10.12.2018
 */
public class DishesListPresenter implements DishesListMVP.Presenter {

    private final DishesListMVP.Interactor interactor;
    private DishesListMVP.View view;

    public DishesListPresenter() {
        this.interactor = new DishesListInteractor();
    }

    @Override
    public void attach(final @NonNull DishesListMVP.View view) {
        this.view = view;
    }

    @Override
    public void gotDishesListFromService() {
        interactor.getAllDishesList(this);
    }

    @Override
    public void gotSearchedDishesListFromService(final @NonNull String query) {
        interactor.getAllSearchedDishes(query, this);
    }

    @Override
    public void destroy() {
        view = null;
    }

    @Override
    public void initView() {
        view.showProgressDialog();
        view.setupAddNewMealListener();
        view.setupSearchDishesListener();
        view.setupSwipeListener();
        interactor.getAllDishesList(this);
    }

    @Override
    public void deleteSwipedDish(int dishId, List<DishesApiModel> dishesList) {
        interactor.deleteDish(dishId, dishesList, this);
    }

    @Override
    public void onGetDishesListSuccesCallback(final @NonNull List<DishesApiModel> dishesList) {
        view.dismissProgressDialog();
        view.showDishesList(dishesList);
    }

    @Override
    public void onGetDishesListErrorCallback(final @NonNull ApiError apiError) {
        view.dismissProgressDialog();
        view.showError(apiError.getMessage());
    }

    @Override
    public void onDeletingDishSuccesCallback(final @NonNull List<DishesApiModel> dishesList) {
        view.showDishesList(dishesList);
    }

    @Override
    public void onDeletingDishErrorCallback(final @NonNull ApiError apiError) {
        view.showError(apiError.getMessage());
    }
}
