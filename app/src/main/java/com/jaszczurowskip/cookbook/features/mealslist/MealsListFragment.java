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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentMealsListBinding;
import com.jaszczurowskip.cookbook.datasource.model.DishModelToPost;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.datasource.retrofit.ApiService;
import com.jaszczurowskip.cookbook.datasource.retrofit.RetrofitClient;
import com.jaszczurowskip.cookbook.features.addnewmeal.AddNewMealActivity;
import com.jaszczurowskip.cookbook.utils.Utils;
import com.jaszczurowskip.cookbook.utils.rx.AppSchedulersProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DefaultObserver;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealsListFragment extends Fragment {
    private FragmentMealsListBinding fragmentMealsListBinding;
    private Retrofit retrofit;
    private ApiService apiService;
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(fragmentMealsListBinding.mealsListRecycler);
        fetchDataFromRemote();
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
                dishesList.clear();
                fetchDataFromRemote();
            }
        };
    }

    private void deleteDish(int position) {
        apiService.deleteDish(dishesList.get(position).getId())
                .subscribeOn(AppSchedulersProvider.getInstance().io())
                .observeOn(AppSchedulersProvider.getInstance().ui())
                .retryWhen(throwables -> throwables.delay(2, TimeUnit.SECONDS))
                .subscribe(new DefaultObserver<DishModelToPost>() {
                    @Override
                    public void onNext(DishModelToPost dishModelToPost) {
                        //no-op
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "Can't delete dish", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getContext(), "Dish deleted", Toast.LENGTH_LONG).show();
                    }
                });
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
        apiService.getSearchedDishes(String.valueOf(searchingParameter))
                .subscribeOn(AppSchedulersProvider.getInstance().io())
                .observeOn(AppSchedulersProvider.getInstance().ui())
                .retryWhen(throwables -> throwables.delay(2, TimeUnit.SECONDS))
                .subscribe(new Observer<List<DishesApiModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

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

                    }
                });
    }

    private void fetchDataFromRemote() {
        apiService.getAllDishes()
                .subscribeOn(AppSchedulersProvider.getInstance().io())
                .observeOn(AppSchedulersProvider.getInstance().ui())
                .retryWhen(throwables -> throwables.delay(2, TimeUnit.SECONDS))
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
        dishesList = dishes;
        adapter = new ListAdapter(getContext(), dishesList);
        fragmentMealsListBinding.mealsListRecycler.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentMealsListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_meals_list, container, false);
        return fragmentMealsListBinding.getRoot();
    }
}
