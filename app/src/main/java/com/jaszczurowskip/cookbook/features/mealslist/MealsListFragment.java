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
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.datasource.retrofit.ApiService;
import com.jaszczurowskip.cookbook.datasource.retrofit.RetrofitClient;
import com.jaszczurowskip.cookbook.features.addnewmeal.AddNewMealActivity;
import com.jaszczurowskip.cookbook.utils.Utils;
import com.jaszczurowskip.cookbook.utils.rx.AppSchedulersProvider;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealsListFragment extends Fragment {
    private ListAdapter adapter;
    private FragmentMealsListBinding fragmentMealsListBinding;
    Retrofit retrofit;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiService apiService;
    List<DishesApiModel> dishes;

    public MealsListFragment() {
        // Required empty public constructor
    }

    public static MealsListFragment newInstance() {
        return new MealsListFragment();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrofit = RetrofitClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);
        compositeDisposable.add(apiService.getAllDishes()
                .subscribeOn(AppSchedulersProvider.getInstance().io())
                .observeOn(AppSchedulersProvider.getInstance().ui())
                .subscribe(new Consumer<List<DishesApiModel>>() {
                    @Override
                    public void accept(List<DishesApiModel> apiModels) throws Exception {
                        displayData(apiModels);
                    }
                }));
        fragmentMealsListBinding.addNewMealFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startAnotherActivity(getContext(), AddNewMealActivity.class);
            }
        });

    }

    private void displayData(List<DishesApiModel> apiModels) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        fragmentMealsListBinding.mealsListRecycler.setLayoutManager(layoutManager);
        adapter = new ListAdapter(getContext(),apiModels);
        fragmentMealsListBinding.mealsListRecycler.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMealsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_meals_list, container, false);
        return fragmentMealsListBinding.getRoot();
    }
}
