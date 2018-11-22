package com.jaszczurowskip.cookbook.datasource.retrofit;

import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.datasource.model.IngredientApiModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by jaszczurowskip on 21.11.2018
 */
public interface ApiService {

    @GET("dishes")
    Observable<List<DishesApiModel>> getAllDishes();

    @GET("ingredients")
    Observable<List<IngredientApiModel>> getDishIngredients();



}
