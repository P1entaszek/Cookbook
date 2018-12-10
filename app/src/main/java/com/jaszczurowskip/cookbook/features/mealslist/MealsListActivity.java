package com.jaszczurowskip.cookbook.features.mealslist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.utils.BaseActivity;

import java.util.ArrayList;

public class MealsListActivity extends BaseActivity {
    private static final String MEAL_LIST_FRAG = "MEAL_LIST_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_list);
        FragmentManager manager = getSupportFragmentManager();
        MealsListFragment fragment = (MealsListFragment) manager.findFragmentByTag(MEAL_LIST_FRAG);
        if (fragment == null) {
            fragment = MealsListFragment.newInstance();
        }
        addFragmentToActivity(manager, fragment, R.id.root_activity_meals_list, MEAL_LIST_FRAG);
        centerTitle(this);
    }
}
