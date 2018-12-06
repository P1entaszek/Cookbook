package com.jaszczurowskip.cookbook.features.mealdetails;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentMealDetailsBinding;
import com.jaszczurowskip.cookbook.datasource.CookbookClient;
import com.jaszczurowskip.cookbook.datasource.ServerResponseListener;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.features.IngredientsRecyclerAdapter;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealDetailsFragment extends Fragment {
    private static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    private static final String MEAL_DETAILS_FRAGMENT = MealDetailsFragment.class.getSimpleName();
    private FragmentMealDetailsBinding fragmentMealDetailsBinding;
    private long dishId;
    private Sprite progressBar;
    private IngredientsRecyclerAdapter ingredientsRecyclerAdapter;

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
        this.dishId = Long.parseLong(Objects.requireNonNull(Objects.requireNonNull(args).getString(EXTRA_ITEM_ID)));
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        progressBar = new FadingCircle();
        fragmentMealDetailsBinding.progressBar.setIndeterminateDrawable(progressBar);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        fragmentMealDetailsBinding.recyclerView.setLayoutManager(layoutManager);
        fetchDataFromRemote(dishId);
    }

    private void fetchDataFromRemote(final long dishId) {
        fragmentMealDetailsBinding.progressBar.setVisibility(View.VISIBLE);
        CookbookClient.getCookbookClient().getDish(dishId, new ServerResponseListener<DishesApiModel>() {
            @Override
            public void onSuccess(DishesApiModel dish) {
                fragmentMealDetailsBinding.progressBar.setVisibility(View.INVISIBLE);
                displayData(dish);
            }

            @Override
            public void onError(ApiError error) {
                Log.d(MEAL_DETAILS_FRAGMENT, error.getMessage());
                fragmentMealDetailsBinding.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), R.string.please_check_your_internet_connection, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayData(DishesApiModel dishesApiModel) {
        ingredientsRecyclerAdapter = new IngredientsRecyclerAdapter(getContext(), dishesApiModel.getIngredients());
        fragmentMealDetailsBinding.recyclerView.setAdapter(ingredientsRecyclerAdapter);
        fragmentMealDetailsBinding.mealNameTv.setText(dishesApiModel.getName());
        fragmentMealDetailsBinding.mealDescriptionTv.setText(dishesApiModel.getRecipe());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(getContext())
                .load(dishesApiModel.getPicture()).apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(45))).into(fragmentMealDetailsBinding.mealImg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMealDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_details, container, false);
        return fragmentMealDetailsBinding.getRoot();
    }
}
