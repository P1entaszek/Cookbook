package com.jaszczurowskip.cookbook.features.mealdetails;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.features.IngredientsRecyclerAdapter;
import com.jaszczurowskip.cookbook.features.mealdetails.mvp.MealDetailsMVP;
import com.jaszczurowskip.cookbook.features.mealdetails.mvp.MealDetailsPresenter;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealDetailsFragment extends Fragment implements MealDetailsMVP.View {
    private static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    private static final String MEAL_DETAILS_FRAGMENT = MealDetailsFragment.class.getSimpleName();
    private FragmentMealDetailsBinding fragmentMealDetailsBinding;
    private long dishId;
    private Sprite progressBar;
    private IngredientsRecyclerAdapter ingredientsRecyclerAdapter;
    private MealDetailsPresenter mPresenter;

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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new MealDetailsPresenter();
        mPresenter.attach(this);
        mPresenter.initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMealDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal_details, container, false);
        return fragmentMealDetailsBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroy();
    }

    @Override
    public void setupview() {
        progressBar = new FadingCircle();
        fragmentMealDetailsBinding.progressBar.setIndeterminateDrawable(progressBar);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        fragmentMealDetailsBinding.recyclerView.setLayoutManager(layoutManager);
        mPresenter.getMealsListFromService(dishId);
    }

    @Override
    public void showProgressDialog() {
        fragmentMealDetailsBinding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressDialog() {
        fragmentMealDetailsBinding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayList(DishesApiModel dishesApiModel) {
        ingredientsRecyclerAdapter = new IngredientsRecyclerAdapter(getContext(), dishesApiModel.getIngredients());
        fragmentMealDetailsBinding.recyclerView.setAdapter(ingredientsRecyclerAdapter);
        fragmentMealDetailsBinding.mealNameTv.setText(dishesApiModel.getName());
        fragmentMealDetailsBinding.mealDescriptionTv.setText(dishesApiModel.getRecipe());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(getContext())
                .load(dishesApiModel.getPicture())
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(45)))
                .into(fragmentMealDetailsBinding.mealImg);
    }
}
