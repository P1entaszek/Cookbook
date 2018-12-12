package com.jaszczurowskip.cookbook.datasource.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jaszczurowskip on 20.11.2018
 */
public class DishesApiModel {
    private long id;
    @NonNull
    private String name;
    @NonNull
    private String picture;
    @NonNull
    private String recipe;
    @NonNull
    private List<IngredientApiModel> ingredients;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

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
    public List<IngredientApiModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(final @NonNull List<IngredientApiModel> ingredients) {
        this.ingredients = ingredients;
    }
}