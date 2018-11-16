package com.jaszczurowskip.cookbook.features.mealdetails;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentMealDetailsBinding;
import com.jaszczurowskip.cookbook.features.IngredientsGridAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealDetailsFragment extends Fragment {
    private FragmentMealDetailsBinding fragmentMealDetailsBinding;
    String[] list = {
            "Google", "Github", "Instagram", "Facebook", "Flickr", "Pinterest", "Quora", "Twitter", "Vimeo", "WordPress", "Youtube", "Stumbleupon", "SoundCloud", "Reddit"
    } ;

    public MealDetailsFragment() {
        // Required empty public constructor
    }

    public static MealDetailsFragment newInstance() {
        return new MealDetailsFragment();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IngredientsGridAdapter adapter = new IngredientsGridAdapter(getContext(), list);
        fragmentMealDetailsBinding.ingredientsGridview.setAdapter(adapter);
        fragmentMealDetailsBinding.mealNameTv.setText("some meal name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMealDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_details, container, false);
        return fragmentMealDetailsBinding.getRoot();
    }

}
