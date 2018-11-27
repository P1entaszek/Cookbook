package com.jaszczurowskip.cookbook.features.mealslist;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentMealsListBinding;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.datasource.retrofit.ApiService;
import com.jaszczurowskip.cookbook.datasource.retrofit.RetrofitClient;
import com.jaszczurowskip.cookbook.features.addnewmeal.AddNewMealActivity;
import com.jaszczurowskip.cookbook.utils.Utils;
import com.jaszczurowskip.cookbook.utils.rx.AppSchedulersProvider;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealsListFragment extends Fragment {
    private FragmentMealsListBinding fragmentMealsListBinding;
    private Retrofit retrofit;
    private ApiService apiService;
    private Sprite progressBar;

    public MealsListFragment() {
        // Required empty public constructor
    }

    public static MealsListFragment newInstance() {
        return new MealsListFragment();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRetrofit();
        initView();
        setListeners();
    }

    private void initRetrofit() {
        retrofit = RetrofitClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);
    }

    private void initView() {
        progressBar = new FadingCircle();
        fragmentMealsListBinding.progressBar.setIndeterminateDrawable(progressBar);
        fetchDataFromRemote();
    }

    private void setListeners() {
        fragmentMealsListBinding.addNewMealFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startAnotherActivity(getContext(), AddNewMealActivity.class);
            }
        });
    }

    private void fetchDataFromRemote(){
        apiService.getAllDishes()
                .subscribeOn(AppSchedulersProvider.getInstance().io())
                .observeOn(AppSchedulersProvider.getInstance().ui())
                .subscribe(new Observer<List<DishesApiModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    //no-op
                    }

                    @Override
                    public void onNext(List<DishesApiModel> list) {
                        displayData(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), R.string.Please_check_your_internet_connection, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        fragmentMealsListBinding.progressBar.setVisibility(View.INVISIBLE);
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
