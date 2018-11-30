package com.jaszczurowskip.cookbook.datasource.model;

import java.util.List;

/**
 * Created by jaszczurowskip on 30.11.2018
 */
public class DishModelToPost {
    private String name;
    private String picture;
    private String recipe;
    private List<Long> ingredientIds;

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

    public List<Long> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(List<Long> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }
}
