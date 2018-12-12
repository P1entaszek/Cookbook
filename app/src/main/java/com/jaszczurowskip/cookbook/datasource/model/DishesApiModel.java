package com.jaszczurowskip.cookbook.datasource.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jaszczurowskip on 20.11.2018
 */
public class DishesApiModel {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("picture")
    private String picture;
    @SerializedName("recipe")
    private String recipe;
    @SerializedName("ingredients")
    private List<IngredientApiModel> ingredients;

    public long getId() {
        return id;
    }

    public void setId(final @NonNull long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final @NonNull String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(final @NonNull String picture) {
        this.picture = picture;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(final @NonNull String recipe) {
        this.recipe = recipe;
    }

    public List<IngredientApiModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(final @Nullable List<IngredientApiModel> ingredients) {
        this.ingredients = ingredients;
    }
}