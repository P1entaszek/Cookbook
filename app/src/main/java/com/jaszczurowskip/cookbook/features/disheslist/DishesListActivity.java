package com.jaszczurowskip.cookbook.features.disheslist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.utils.BaseActivity;

public class DishesListActivity extends BaseActivity {
    private static final String MEAL_LIST_FRAG = "MEAL_LIST_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_list);
        FragmentManager manager = getSupportFragmentManager();
        DishesListFragment fragment = (DishesListFragment) manager.findFragmentByTag(MEAL_LIST_FRAG);
        if (fragment == null) {
            fragment = DishesListFragment.newInstance();
        }
        addFragmentToActivity(manager, fragment, R.id.root_activity_meals_list, MEAL_LIST_FRAG);
        centerTitle(this);
    }
}
