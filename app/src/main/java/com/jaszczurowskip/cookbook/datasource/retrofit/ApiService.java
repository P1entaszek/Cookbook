package com.jaszczurowskip.cookbook.datasource.retrofit;

import android.support.annotation.NonNull;

import com.jaszczurowskip.cookbook.datasource.model.DishModelToPost;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.datasource.model.IngredientApiModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jaszczurowskip on 21.11.2018
 */
public interface ApiService {

    @GET("dishes")
    Observable<List<DishesApiModel>> getAllDishes();

    @GET("ingredients")
    Observable<List<IngredientApiModel>> getAllIngredients();

    @GET("dishes/{dishId}")
    Observable<DishesApiModel> getDish(@Path("dishId") final long dishID);

    @GET("dishes?")
    Observable<List<DishesApiModel>> getSearchedDishes(@Query("term") final @NonNull String term);

    @POST("ingredients")
    Observable<String> postIngredient(@Body final @NonNull String string);

    @POST("dishes")
    Observable<DishModelToPost> postDish(@Body final @NonNull DishModelToPost dish);

    @DELETE("dishes/{dishId}")
    Observable<DishModelToPost> deleteDish(@Path("dishId") final long dishID);
}
