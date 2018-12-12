package com.jaszczurowskip.cookbook.datasource.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by jaszczurowskip on 30.11.2018
 */
public class DishModelToPost {
    @NonNull
    private String name;
    @NonNull
    private String picture;
    @NonNull
    private String recipe;
    @NonNull
    private List<Long> ingredientIds;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(final @NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getPicture() {
        return picture;
    }

    public void setPicture(final @NonNull String picture) {
        this.picture = picture;
    }

    @NonNull
    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(final @NonNull String recipe) {
        this.recipe = recipe;
    }

    @NonNull
    public List<Long> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(final @NonNull List<Long> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }
}
