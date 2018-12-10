package com.jaszczurowskip.cookbook.features.dishdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.utils.BaseActivity;

public class DishDetailsActivity extends BaseActivity {
    private static final String MEAL_DETAILS_FRAG = "MEAL_DETAILS_FRAG";
    private static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    @Override
    protected void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        Intent i = getIntent();
        if (i.hasExtra(EXTRA_ITEM_ID)) {
            String dish = i.getStringExtra(EXTRA_ITEM_ID);
            FragmentManager manager = getSupportFragmentManager();
            DishDetailsFragment fragment = (DishDetailsFragment) manager.findFragmentByTag(MEAL_DETAILS_FRAG);
            if (fragment == null) {
                fragment = DishDetailsFragment.newInstance(dish);
            }
            addFragmentToActivity(manager, fragment, R.id.root_activity_meal_details, MEAL_DETAILS_FRAG);
        }
        centerTitle(this);
    }

}
