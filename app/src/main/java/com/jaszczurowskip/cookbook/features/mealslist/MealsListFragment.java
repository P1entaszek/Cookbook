package com.jaszczurowskip.cookbook.features.mealslist;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentMealsListBinding;
import com.jaszczurowskip.cookbook.features.addnewmeal.AddNewMealActivity;
import com.jaszczurowskip.cookbook.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealsListFragment extends Fragment {
    private ListAdapter adapter;
    private FragmentMealsListBinding fragmentMealsListBinding;

    public MealsListFragment() {
        // Required empty public constructor
    }

    public static MealsListFragment newInstance() {
        return new MealsListFragment();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        fragmentMealsListBinding.mealsListRecycler.setLayoutManager(layoutManager);
        adapter = new ListAdapter(getContext());
        fragmentMealsListBinding.mealsListRecycler.setAdapter(adapter);
        fragmentMealsListBinding.addNewMealFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startAnotherActivity(getContext(), AddNewMealActivity.class);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMealsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_meals_list, container, false);
        return fragmentMealsListBinding.getRoot();
    }
}
