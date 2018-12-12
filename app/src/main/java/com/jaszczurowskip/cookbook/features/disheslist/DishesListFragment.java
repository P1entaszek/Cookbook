package com.jaszczurowskip.cookbook.features.disheslist;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.jaszczurowskip.cookbook.features.addnewdish.AddNewDishActivity;
import com.jaszczurowskip.cookbook.features.dishdetails.mvp.DishDetailsPresenter;
import com.jaszczurowskip.cookbook.features.disheslist.mvp.DishesListMVP;
import com.jaszczurowskip.cookbook.features.disheslist.mvp.DishesListPresenter;
import com.jaszczurowskip.cookbook.utils.Utils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DishesListFragment extends Fragment implements DishesListMVP.View{
    private FragmentMealsListBinding fragmentMealsListBinding;
    private DishesListMVP.Presenter presenter;

    public DishesListFragment() {
        // Required empty public constructor
    }

    public static DishesListFragment newInstance() {
        return new DishesListFragment();
    }

    public void onActivityCreated(final @Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(final @NonNull View view, final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new DishesListPresenter();
        presenter.attach(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.initView();
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

    @Override
    public void setupAddNewMealListener() {
        fragmentMealsListBinding.addNewMealFab.setOnClickListener(v -> Utils.startAnotherActivity(getContext(), AddNewDishActivity.class));
    }

    @Override
    public  void setupSearchDishesListener() {
        fragmentMealsListBinding.searchIngredientsBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                presenter.gotSearchedDishesListFromService(s.toString());
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });
    }

    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater, final @NonNull ViewGroup container,
                             final @Nullable Bundle savedInstanceState) {
        fragmentMealsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_meals_list, container, false);
        return fragmentMealsListBinding.getRoot();
    }

    @Override
    public void showProgressDialog() {
        fragmentMealsListBinding.mealsListRecycler.setVisibility(View.INVISIBLE);
        fragmentMealsListBinding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressDialog() {
        fragmentMealsListBinding.mealsListRecycler.setVisibility(View.VISIBLE);
        fragmentMealsListBinding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(final @NonNull String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDishesList(final @NonNull List<DishesApiModel> dishesList) {
        ListAdapter adapter;
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentMealsListBinding.mealsListRecycler.setLayoutManager(layoutManager);
        adapter = new ListAdapter(getContext(), dishesList);
        fragmentMealsListBinding.mealsListRecycler.setAdapter(adapter);
    }
}
