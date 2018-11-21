package com.jaszczurowskip.cookbook.features.mealdetails;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentMealDetailsBinding;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.datasource.model.IngredientApiModel;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealDetailsFragment extends Fragment {
    private FragmentMealDetailsBinding fragmentMealDetailsBinding;
    private static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    private DishesApiModel dish;
    private String retrievedGSon;
    String[] list = {
            "Google", "Github", "Instagram", "Facebook", "Flickr", "Pinterest", "Quora", "Twitter", "Vimeo", "WordPress", "Youtube", "Stumbleupon", "SoundCloud", "Reddit"
    } ;

    public MealDetailsFragment() {
        // Required empty public constructor
    }

    public static MealDetailsFragment newInstance(final String dish) {
        MealDetailsFragment mealDetailsFragment = new MealDetailsFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_ITEM_ID, dish);

        mealDetailsFragment.setArguments(args);
        return mealDetailsFragment;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        Gson gson = new Gson();
        this.retrievedGSon = Objects.requireNonNull(args).getString(EXTRA_ITEM_ID);
        this.dish =  gson.fromJson(retrievedGSon, DishesApiModel.class);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<IngredientApiModel> ingredientList;
        ingredientList = dish.getIngredients();
        IngredientsDetailsGridAdapter adapter = new IngredientsDetailsGridAdapter(getContext(), ingredientList);
        fragmentMealDetailsBinding.ingredientsGridview.setAdapter(adapter);
        fragmentMealDetailsBinding.mealNameTv.setText(dish.getName());
        Glide.with(getContext()).load(dish.getPicture()).into(fragmentMealDetailsBinding.mealImg);
        fragmentMealDetailsBinding.mealDescriptionTv.setText(dish.getRecipe());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMealDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_details, container, false);
        return fragmentMealDetailsBinding.getRoot();
    }

}
