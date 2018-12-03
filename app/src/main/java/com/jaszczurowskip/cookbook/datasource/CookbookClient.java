package com.jaszczurowskip.cookbook.datasource;

import com.google.gson.Gson;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishModelToPost;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.datasource.retrofit.ApiService;
import com.jaszczurowskip.cookbook.datasource.retrofit.RetrofitClient;
import com.jaszczurowskip.cookbook.utils.rx.AppSchedulersProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DefaultObserver;
import retrofit2.HttpException;
import retrofit2.Retrofit;

/**
 * Created by jaszczurowskip on 03.12.2018
 */
public class CookbookClient {
    private static CookbookClient cookbookClient;

    public static CookbookClient getCookbookClient() {
        if (cookbookClient == null) {
            cookbookClient = new CookbookClient();
        }
        return cookbookClient;
    }

    private ApiService getApiService() {
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService;
    }

    private void castErrorToHTTP(final Throwable e, final ServerResponseListener<?> listener) {
        if (e instanceof HttpException) {
            try {
                e.printStackTrace();
                String body = ((HttpException) e).response().errorBody().string();
                Gson gson = new Gson();
                ApiError error = gson.fromJson(body, ApiError.class);
                listener.onError(error);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        e.printStackTrace();
    }

    public void getAllDishes(final ServerResponseListener<List<DishesApiModel>> listener) {
        getApiService()
                .getAllDishes()
                .subscribeOn(AppSchedulersProvider.getInstance().io())
                .observeOn(AppSchedulersProvider.getInstance().ui())
                .retryWhen(throwables -> throwables.delay(2, TimeUnit.SECONDS))
                .subscribe(new Observer<List<DishesApiModel>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final List<DishesApiModel> list) {
                        listener.onSuccess(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        castErrorToHTTP(e, listener);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getSearchedDishes(final CharSequence searchingParameter, final ServerResponseListener<List<DishesApiModel>> listener) {
        getApiService().getSearchedDishes(String.valueOf(searchingParameter))
                .subscribeOn(AppSchedulersProvider.getInstance().io())
                .observeOn(AppSchedulersProvider.getInstance().ui())
                .retryWhen(throwables -> throwables.delay(2, TimeUnit.SECONDS))
                .subscribe(new Observer<List<DishesApiModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<DishesApiModel> list) {
                        listener.onSuccess(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        castErrorToHTTP(e, listener);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteDish(int selectedDish, List<DishesApiModel> list, final ServerResponseListener<List<DishesApiModel>> listener) {
        getApiService().deleteDish(list.get(selectedDish).getId())
                .subscribeOn(AppSchedulersProvider.getInstance().io())
                .observeOn(AppSchedulersProvider.getInstance().ui())
                .retryWhen(throwables -> throwables.delay(2, TimeUnit.SECONDS))
                .subscribe(new DefaultObserver<DishModelToPost>() {
                    @Override
                    public void onNext(DishModelToPost dishModelToPost) {
                        //no-op
                    }

                    @Override
                    public void onError(Throwable e) {
                        castErrorToHTTP(e, listener);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
