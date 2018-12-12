package com.jaszczurowskip.cookbook.datasource.model;

import android.support.annotation.NonNull;

/**
 * Created by jaszczurowskip on 20.11.2018
 */

public class IngredientApiModel {
    private long id;
    @NonNull
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(final @NonNull String name) {
        this.name = name;
    }
}
