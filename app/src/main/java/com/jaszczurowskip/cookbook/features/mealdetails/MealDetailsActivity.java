package com.jaszczurowskip.cookbook.features.mealdetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.utils.BaseActivity;

import java.util.ArrayList;

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
        centerTitle();
    }

    private void centerTitle() {
        ArrayList<View> textViews = new ArrayList<>();

        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);

        if(textViews.size() > 0) {
            AppCompatTextView appCompatTextView = null;
            if(textViews.size() == 1) {
                appCompatTextView = (AppCompatTextView) textViews.get(0);
            } else {
                for(View v : textViews) {
                    if(v.getParent() instanceof Toolbar) {
                        appCompatTextView = (AppCompatTextView) v;
                        break;
                    }
                }
            }

            if(appCompatTextView != null) {
                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                appCompatTextView.setLayoutParams(params);
                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
        }
    }
}
