package com.jaszczurowskip.cookbook.features.mealslist;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentMealsListBinding;
import com.jaszczurowskip.cookbook.datasource.CookbookClient;
import com.jaszczurowskip.cookbook.datasource.ServerResponseListener;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.features.addnewmeal.AddNewMealActivity;
import com.jaszczurowskip.cookbook.utils.Utils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealsListFragment extends Fragment {
    private FragmentMealsListBinding fragmentMealsListBinding;
    private String MEALS_LIST_FRAGMENT = MealsListFragment.class.getSimpleName();
    private Sprite progressBar;
    private List<DishesApiModel> dishesList;

    public MealsListFragment() {
        // Required empty public constructor
    }

    public static MealsListFragment newInstance() {
        return new MealsListFragment();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setListeners();
    }


    @Override
    public void onStart() {
        super.onStart();
        recyclerLoadingVisibility(true);
        fragmentMealsListBinding.searchIngredientsBar.setText("");
        fetchDataFromRemote();
    }

    private void initView() {
        progressBar = new FadingCircle();
        fragmentMealsListBinding.progressBar.setIndeterminateDrawable(progressBar);
        /*ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(fragmentMealsListBinding.mealsListRecycler);*/
        fetchDataFromRemote();
    }

   /* private ItemTouchHelper.Callback createHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView1, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, @NonNull int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                deleteDish(position);
                dishesList.clear();
                fetchDataFromRemote();
            }
        };
    }

    private void deleteDish(int position) {
        CookbookClient.getCookbookClient().deleteDish(position, dishesList, new ServerResponseListener<List<DishesApiModel>>() {
            @Override
            public void onSuccess(List<DishesApiModel> response) {
                Toast.makeText(getContext(), R.string.dish_deleted, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(ApiError error) {
                Log.d(MEALS_LIST_FRAGMENT, error.getMessage());
                Toast.makeText(getContext(), R.string.cant_delete_dish, Toast.LENGTH_LONG).show();
            }
        });
    }*/

    private void recyclerLoadingVisibility(boolean isRecyclerLoading) {
        if (isRecyclerLoading) {
            fragmentMealsListBinding.mealsListRecycler.setVisibility(View.INVISIBLE);
            fragmentMealsListBinding.progressBar.setVisibility(View.VISIBLE);
        }
        if (!isRecyclerLoading) {
            fragmentMealsListBinding.mealsListRecycler.setVisibility(View.VISIBLE);
            fragmentMealsListBinding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setListeners() {
        addNewMeal();
        searchDishes();
    }

    private void addNewMeal() {
        fragmentMealsListBinding.addNewMealFab.setOnClickListener(v -> Utils.startAnotherActivity(getContext(), AddNewMealActivity.class));
    }

    private void searchDishes() {
        fragmentMealsListBinding.searchIngredientsBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSearchedDishesList(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getSearchedDishesList(CharSequence searchingParameter) {
        recyclerLoadingVisibility(true);
        CookbookClient.getCookbookClient().getSearchedDishes(searchingParameter, new ServerResponseListener<List<DishesApiModel>>() {
            @Override
            public void onSuccess(List<DishesApiModel> list) {
                dishesList = list;
                displayData(dishesList);
                recyclerLoadingVisibility(false);
            }

            @Override
            public void onError(ApiError error) {
                Log.d(MEALS_LIST_FRAGMENT, error.getMessage());
                recyclerLoadingVisibility(false);
                Toast.makeText(getContext(), R.string.please_check_your_internet_connection, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchDataFromRemote() {
        CookbookClient.getCookbookClient().getAllDishes(new ServerResponseListener<List<DishesApiModel>>() {
            @Override
            public void onSuccess(List<DishesApiModel> list) {
                dishesList = list;
                displayData(dishesList);
                recyclerLoadingVisibility(false);
            }

            @Override
            public void onError(ApiError error) {
                Log.d(MEALS_LIST_FRAGMENT, error.getMessage());
                recyclerLoadingVisibility(false);
                Toast.makeText(getContext(), R.string.please_check_your_internet_connection, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayData(List<DishesApiModel> dishes) {
        ListAdapter adapter;
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentMealsListBinding.mealsListRecycler.setLayoutManager(layoutManager);
        adapter = new ListAdapter(getContext(), dishes);
        fragmentMealsListBinding.mealsListRecycler.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMealsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_meals_list, container, false);
        return fragmentMealsListBinding.getRoot();
    }
}
