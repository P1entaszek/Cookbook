package com.jaszczurowskip.cookbook.datasource.model;

/**
 * Created by jaszczurowskip on 20.11.2018
 */
public class IngredientApiModel {
    private long id;
    private String name;

    public IngredientApiModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
}
