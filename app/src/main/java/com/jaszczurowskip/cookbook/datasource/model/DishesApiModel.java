package com.jaszczurowskip.cookbook.datasource.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jaszczurowskip on 20.11.2018
 */
public class DishesApiModel implements Serializable{
    private long id;
    private String name;
    private String picture;
    private String recipe;
    private List<IngredientApiModel> ingredients;

    public DishesApiModel(long id, String name, String picture, String recipe, List<IngredientApiModel> ingredients) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getPicture() { return picture; }
    public void setPicture(String value) { this.picture = value; }

    public String getRecipe() { return recipe; }
    public void setRecipe(String value) { this.recipe = value; }

    public List<IngredientApiModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientApiModel> ingredients) {
        this.ingredients = ingredients;
    }
}

