package com.jaszczurowskip.cookbook.features.addnewmeal;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentAddNewMealBinding;
import com.jaszczurowskip.cookbook.features.IngredientsGridAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewMealFragment extends Fragment {
    private FragmentAddNewMealBinding fragmentAddNewMealBinding;
    String[] list = {
            "Google",
            "Github",
            "Instagram",
            "Facebook",
            "Flickr",
            "Pinterest",
            "Quora",
            "Twitter",
            "Vimeo",
            "WordPress",
            "Youtube",
            "Stumbleupon",
            "SoundCloud",
            "Reddit",
            "Blogger"
    } ;

    public AddNewMealFragment() {
        // Required empty public constructor
    }

    public static AddNewMealFragment newInstance() {
        return new AddNewMealFragment();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IngredientsGridAdapter adapter = new IngredientsGridAdapter(getContext(), list);
        fragmentAddNewMealBinding.ingredientsGridview.setAdapter(adapter);
        fragmentAddNewMealBinding.mealNameTv.setText("some meal name mate!");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAddNewMealBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_meal, container, false);
        return fragmentAddNewMealBinding.getRoot();
    }
}
