package com.jaszczurowskip.cookbook.datasource.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jaszczurowskip on 06.11.2018
 */
public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getRetrofitInstance()
    {
        if(instance==null){
            instance = new Retrofit.Builder().baseUrl("https://cookbook-koszalin.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return instance;
    }

    private RetrofitClient() {
    }
}
