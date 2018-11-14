package com.jaszczurowskip.cookbook.features.mealdetails;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaszczurowskip.cookbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealDetailsFragment extends Fragment {

    public MealDetailsFragment() {
        // Required empty public constructor
    }

    public static MealDetailsFragment newInstance() {
        return new MealDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

}
