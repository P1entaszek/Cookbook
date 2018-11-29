package com.jaszczurowskip.cookbook.datasource.model;

import java.util.List;

/**
 * Created by jaszczurowskip on 20.11.2018
 */
public class DishesApiModel {
    private long id;
    private String name;
    private String picture;
    private String recipe;
    private List<IngredientApiModel> ingredients;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public List<IngredientApiModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientApiModel> ingredients) {
        this.ingredients = ingredients;
    }
}