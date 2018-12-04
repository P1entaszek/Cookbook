package com.jaszczurowskip.cookbook.features.addnewmeal;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.utils.BaseActivity;

import java.util.ArrayList;

public class AddNewMealActivity extends BaseActivity {
    private static final String ADD_NEW_MEAL_FRAG = "ADD_NEW_MEAL_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meal);
        FragmentManager manager = getSupportFragmentManager();
        AddNewMealFragment fragment = (AddNewMealFragment) manager.findFragmentByTag(ADD_NEW_MEAL_FRAG);
        if (fragment == null) {
            fragment = AddNewMealFragment.newInstance();
        }
        addFragmentToActivity(manager, fragment, R.id.root_activity_add_meal, ADD_NEW_MEAL_FRAG);
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
