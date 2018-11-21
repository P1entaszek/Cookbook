package com.jaszczurowskip.cookbook.features.mealdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.utils.BaseActivity;

public class MealDetailsActivity extends BaseActivity {
    private static final String MEAL_DETAILS_FRAG = "MEAL_DETAILS_FRAG";
    private static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        Intent i = getIntent();
        if (i.hasExtra(EXTRA_ITEM_ID)) {
            String dish = i.getStringExtra(EXTRA_ITEM_ID);
            FragmentManager manager = getSupportFragmentManager();
            MealDetailsFragment fragment = (MealDetailsFragment) manager.findFragmentByTag(MEAL_DETAILS_FRAG);
            if (fragment == null) {
                fragment = MealDetailsFragment.newInstance(dish);
            }
            addFragmentToActivity(manager, fragment, R.id.root_activity_meal_details, MEAL_DETAILS_FRAG);
        }
    }
}
