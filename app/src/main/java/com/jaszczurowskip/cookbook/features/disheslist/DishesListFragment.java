package com.jaszczurowskip.cookbook.features.disheslist;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentDishesListBinding;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.features.addnewdish.AddNewDishActivity;
import com.jaszczurowskip.cookbook.features.disheslist.mvp.DishesListMVP;
import com.jaszczurowskip.cookbook.features.disheslist.mvp.DishesListPresenter;
import com.jaszczurowskip.cookbook.utils.Utils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DishesListFragment extends Fragment implements DishesListMVP.View {
    @NonNull
    private FragmentDishesListBinding fragmentDishesListBinding;
    @NonNull
    private DishesListMVP.Presenter presenter;
    @Nullable
    private List<DishesApiModel> dishesList;

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

    private ItemTouchHelper.Callback createHelperCallback() {
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
            }
        };
    }

    private void deleteDish(int position) {
        presenter.deleteSwipedDish(position, dishesList);
    }

    @Override
    public void setupAddNewMealListener() {
        fragmentDishesListBinding.addNewMealFab.setOnClickListener(v -> Utils.startAnotherActivity(getContext(), AddNewDishActivity.class));
    }

    @Override
    public void setupSwipeListener() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(fragmentDishesListBinding.mealsListRecycler);
    }

    @Override
    public void setupSearchDishesListener() {
        fragmentDishesListBinding.searchIngredientsBar.addTextChangedListener(new TextWatcher() {
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
        fragmentDishesListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dishes_list, container, false);
        return fragmentDishesListBinding.getRoot();
    }

    @Override
    public void showProgressDialog() {
        fragmentDishesListBinding.mealsListRecycler.setVisibility(View.INVISIBLE);
        fragmentDishesListBinding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressDialog() {
        fragmentDishesListBinding.mealsListRecycler.setVisibility(View.VISIBLE);
        fragmentDishesListBinding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(final @NonNull String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDishesList(final @NonNull List<DishesApiModel> dishesList) {
        this.dishesList = dishesList;
        ListAdapter adapter;
        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentDishesListBinding.mealsListRecycler.setLayoutManager(layoutManager);
        adapter = new ListAdapter(getContext(), dishesList);
        fragmentDishesListBinding.mealsListRecycler.setAdapter(adapter);
    }
}
