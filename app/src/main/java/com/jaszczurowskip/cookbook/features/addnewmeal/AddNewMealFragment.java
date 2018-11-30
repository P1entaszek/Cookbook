package com.jaszczurowskip.cookbook.features.addnewmeal;


import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentAddNewMealBinding;
import com.jaszczurowskip.cookbook.datasource.model.DishModelToPost;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.datasource.model.IngredientApiModel;
import com.jaszczurowskip.cookbook.datasource.retrofit.ApiService;
import com.jaszczurowskip.cookbook.datasource.retrofit.RetrofitClient;
import com.jaszczurowskip.cookbook.features.IngredientsRecyclerAdapter;
import com.jaszczurowskip.cookbook.utils.rx.AppSchedulersProvider;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewMealFragment extends Fragment {
    private static int RESULT_LOAD_IMG = 0;
    private FragmentAddNewMealBinding fragmentAddNewMealBinding;
    private ArrayList<String> listSpinnerItems = new ArrayList<>();
    private LinkedHashSet<String> addingNewIngredientToDishHashSet = new LinkedHashSet<>();
    private Retrofit retrofit;
    private ApiService apiService;
    private ArrayList<IngredientApiModel> ingredientArrayList = new ArrayList<>();
    private ArrayList<IngredientApiModel> choosenIngredients = new ArrayList<>();
    private Bitmap selectedImage;


    public AddNewMealFragment() {
        // Required empty public constructor
    }

    public static AddNewMealFragment newInstance() {
        return new AddNewMealFragment();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initApiService();
        initView();
        fetchIngredientsFromRemoteWithSpinner();
        photoPickerListener();
        addNewIngredientToSpinner();
        addNewIngredientToDish();
        sendNewDishToRemote();

    }

    public void sendNewDishToRemote(){
        fragmentAddNewMealBinding.acceptAddingMealBttn.setOnClickListener(v -> {
             apiService.postDish(preparedNewDishToPost())
                     .subscribeOn(AppSchedulersProvider.getInstance().io())
                     .observeOn(AppSchedulersProvider.getInstance().ui())
                     .retryWhen(throwables -> throwables.delay(1, TimeUnit.MICROSECONDS))
                     .subscribe(new Observer<DishModelToPost>() {
                         @Override
                         public void onSubscribe(Disposable d) {
                            //no-op
                         }

                         @Override
                         public void onNext(DishModelToPost dish) {
                            //no-op
                         }

                         @Override
                         public void onError(Throwable e) {
                            e.printStackTrace();
                             Toast.makeText(getContext(), R.string.Please_check_your_internet_connection, Toast.LENGTH_LONG).show();
                         }

                         @Override
                         public void onComplete() {
                             Toast.makeText(getContext(), R.string.dish_added, Toast.LENGTH_LONG).show();
                         }
                     });
        });
    }
    private DishModelToPost preparedNewDishToPost() {
        String typeOfData = "data:image/jpeg;base64,";
        String preparedImageToPost = typeOfData.concat(encodeToBase64(selectedImage, Bitmap.CompressFormat.JPEG, 5));
        DishModelToPost newDish = new DishModelToPost();
        newDish.setName(fragmentAddNewMealBinding.mealNameEt.getText().toString());
        newDish.setRecipe(fragmentAddNewMealBinding.mealDescriptionTv.getText().toString());
        newDish.setPicture(preparedImageToPost);
        ArrayList<Long> ingredientsIds = new ArrayList<>();
        for(int i=0; i<choosenIngredients.size(); i++){
            ingredientsIds.add(i, choosenIngredients.get(i).getId());
        }
        newDish.setIngredientIds(ingredientsIds);
        Gson gson = new Gson();
        String s = gson.toJson(newDish);
        return newDish;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }

    private void initView() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        fragmentAddNewMealBinding.ingredientsRv.setLayoutManager(layoutManager);
        listSpinnerItems.add(getString(R.string.choose_some_ingredients));
    }

    private void initApiService() {
        retrofit = RetrofitClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);
    }

    private void fetchIngredientsFromRemoteWithSpinner() {
        apiService.getAllIngredients()
                .subscribeOn(AppSchedulersProvider.getInstance().io())
                .observeOn(AppSchedulersProvider.getInstance().ui())
                .retryWhen(throwables -> throwables.delay(2, TimeUnit.SECONDS))
                .subscribe(new Observer<List<IngredientApiModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //no-op
                    }

                    @Override
                    public void onNext(List<IngredientApiModel> ingredientApiModels) {
                        listSpinnerItems.clear();
                        listSpinnerItems.add(getString(R.string.choose_some_ingredients));
                        for (int i = 0; i < ingredientApiModels.size(); i++) {
                            listSpinnerItems.add(ingredientApiModels.get(i).getName());
                            ingredientArrayList.add(ingredientApiModels.get(i));
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listSpinnerItems);
                        fragmentAddNewMealBinding.ingredientsSpinner.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), R.string.Please_check_your_internet_connection, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                    //no-op
                    }
                });
    }

    private void addNewIngredientToSpinner() {
        fragmentAddNewMealBinding.addIngredientsBttn.setOnClickListener(v -> {
            final EditText taskEditText = new EditText(getContext());
            final AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.please_type_new_ingredient))
                    .setView(taskEditText)
                    .setPositiveButton("Add", (dialog1, which) -> {
                        String ingredient = String.valueOf(taskEditText.getText());
                        if (!ingredient.isEmpty()) {
                            apiService.postIngredient(ingredient)
                                    .subscribeOn(AppSchedulersProvider.getInstance().io())
                                    .observeOn(AppSchedulersProvider.getInstance().ui())
                                    .retryWhen(throwables -> throwables.delay(2, TimeUnit.SECONDS))
                                    .subscribe(new Observer<String>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            //no-op
                                        }

                                        @Override
                                        public void onNext(String s) {
                                            //no-op
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onComplete() {
                                            //no-op
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "New element must contain name", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();
            dialog.show();
        });
    }

    private void addNewIngredientToDish() {
        fragmentAddNewMealBinding.ingredientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals("Choose some ingredients")) {
                    if (addingNewIngredientToDishHashSet.add(parent.getItemAtPosition(position).toString())) {
                        choosenIngredients.add(ingredientArrayList.get(position-1));
                        IngredientsRecyclerAdapter ingredientsRecyclerAdapter = new IngredientsRecyclerAdapter(getContext(), choosenIngredients);
                        fragmentAddNewMealBinding.ingredientsRv.setAdapter(ingredientsRecyclerAdapter);
                    } else {
                        Toast.makeText(getContext(), "Element is in dish", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //no-op
            }
        });
    }

    private void photoPickerListener() {
        fragmentAddNewMealBinding.mealNameImg.setOnClickListener(view -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                fragmentAddNewMealBinding.mealNameImg.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error when upload files", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getContext(), "You don't choose the picture", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAddNewMealBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_meal, container, false);
        return fragmentAddNewMealBinding.getRoot();
    }
}
