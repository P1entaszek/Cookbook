package com.jaszczurowskip.cookbook.datasource.model;

import java.util.List;

import lombok.Data;

/**
 * Created by jaszczurowskip on 20.11.2018
 */
@Data
public class DishesApiModel {
    private long id;
    private String name;
    private String picture;
    private String recipe;
    private List<IngredientApiModel> ingredients;
}