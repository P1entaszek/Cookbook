package com.jaszczurowskip.cookbook.datasource.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Created by jaszczurowskip on 03.12.2018
 */
public class DataContainer<T> {

    @SerializedName("added")
    private List<T> mCreated = Collections.emptyList();

    @SerializedName("updated")
    private List<T> mUpdated = Collections.emptyList();


    public List<T> getCreated() {
        return mCreated;
    }

    public List<T> getUpdated() {
        return mUpdated;
    }
}
