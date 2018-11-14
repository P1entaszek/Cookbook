package com.jaszczurowskip.cookbook.features.mealdetails;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.utils.BaseActivity;

public class MealDetailsActivity extends BaseActivity {
    private static final String MEAL_DETAILS_FRAG = "MEAL_DETAILS_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        FragmentManager manager = getSupportFragmentManager();
        MealDetailsFragment fragment = (MealDetailsFragment) manager.findFragmentByTag(MEAL_DETAILS_FRAG);
        if (fragment == null) {
            fragment = MealDetailsFragment.newInstance();
        }
        addFragmentToActivity(manager, fragment, R.id.root_activity_meal_details, MEAL_DETAILS_FRAG);
    }
}
