package com.jaszczurowskip.cookbook.features.addnewdish;


import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentAddNewMealBinding;
import com.jaszczurowskip.cookbook.datasource.model.DishModelToPost;
import com.jaszczurowskip.cookbook.datasource.model.IngredientApiModel;
import com.jaszczurowskip.cookbook.features.IngredientsRecyclerAdapter;
import com.jaszczurowskip.cookbook.features.addnewdish.mvp.AddNewDishMVP;
import com.jaszczurowskip.cookbook.features.addnewdish.mvp.AddNewDishPresenter;
import com.jaszczurowskip.cookbook.features.disheslist.DishesListActivity;
import com.jaszczurowskip.cookbook.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewDishFragment extends Fragment implements AddNewDishMVP.View {
    private static final int WRONG_DISH_NAME = 0;
    private static final int WRONG_DISH_DESCRIPTION = 1;
    private static final int ERROR_WHEN_ULOAD_PHOTO = 2;
    private static final int DIDNT_CHOOSE_INGREDIENTS = 3;
    private static int RESULT_LOAD_IMG = 0;
    private FragmentAddNewMealBinding fragmentAddNewMealBinding;
    private ArrayList<String> listSpinnerItems = new ArrayList<>();
    private LinkedHashSet<String> addingNewIngredientToDishHashSet = new LinkedHashSet<>();
    private ArrayList<IngredientApiModel> ingredientArrayList = new ArrayList<>();
    private ArrayList<IngredientApiModel> choosenIngredients = new ArrayList<>();
    private Bitmap selectedImage;
    private IngredientsRecyclerAdapter ingredientsRecyclerAdapter;
    private ArrayAdapter<String> spinnerAdapter;
    private AddNewDishMVP.Presenter presenter;


    public AddNewDishFragment() {
        // Required empty public constructor
    }

    public static AddNewDishFragment newInstance() {
        return new AddNewDishFragment();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new AddNewDishPresenter();
        presenter.attach(this);
        presenter.initView();
    }

    private DishModelToPost preparedNewDishToPost() {
        String preparedImageToPost = null;
        if (selectedImage != null) {
            preparedImageToPost = Utils.encodeToBase64(selectedImage, Bitmap.CompressFormat.JPEG, 20);
        }
        DishModelToPost newDish = new DishModelToPost();
        newDish.setName(fragmentAddNewMealBinding.mealNameEt.getText().toString());
        newDish.setRecipe(fragmentAddNewMealBinding.mealDescriptionTv.getText().toString());
        newDish.setPicture(preparedImageToPost);
        ArrayList<Long> ingredientsIds = new ArrayList<>();
        for (int i = 0; i < choosenIngredients.size(); i++) {
            ingredientsIds.add(i, choosenIngredients.get(i).getId());
        }
        newDish.setIngredientIds(ingredientsIds);
        return newDish;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        presenter.getPicture(reqCode, resultCode, data, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAddNewMealBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_meal, container, false);
        return fragmentAddNewMealBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.destroy();
    }

    @Override
    public void prepareIngredientsRecycler() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        fragmentAddNewMealBinding.ingredientsRv.setLayoutManager(layoutManager);
    }

    @Override
    public void showGeneralError(@NonNull String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAllIngredients(@NonNull List<IngredientApiModel> ingredients) {
        listSpinnerItems.clear();
        listSpinnerItems.add(getString(R.string.choose_some_ingredients));
        for (int i = 0; i < ingredients.size(); i++) {
            listSpinnerItems.add(ingredients.get(i).getName());
            ingredientArrayList.add(ingredients.get(i));
        }
        spinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1, listSpinnerItems);
        fragmentAddNewMealBinding.ingredientsSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void setupAddNewDishListener() {
        fragmentAddNewMealBinding.acceptAddingMealBttn.setOnClickListener(v -> {
            if (presenter.validateNewDishBeforePost(getContext(), preparedNewDishToPost())) {
                presenter.sendDishToServer(preparedNewDishToPost());
            }
        });
    }

    @Override
    public void addNewIngredientToDishFromList() {
        fragmentAddNewMealBinding.ingredientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getItemAtPosition(position).toString().equals(getString(R.string.choose_some_ingredients))) {
                    if (addingNewIngredientToDishHashSet.add(parent.getItemAtPosition(position).toString())) {
                        choosenIngredients.add(ingredientArrayList.get(position - 1));
                        ingredientsRecyclerAdapter = new IngredientsRecyclerAdapter(getContext(), choosenIngredients);
                        fragmentAddNewMealBinding.ingredientsRv.setAdapter(ingredientsRecyclerAdapter);
                    } else {
                        Toast.makeText(getContext(), R.string.ingredient_is_in_dish, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //no-op
            }
        });
    }

    @Override
    public void setupAddNewIngredientListener() {
        fragmentAddNewMealBinding.addIngredientsBttn.setOnClickListener(v -> {
            final EditText taskEditText = new EditText(getContext());
            final AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.add_new_ingredient))
                    .setView(taskEditText)
                    .setPositiveButton(R.string.add, (dialog1, which) -> {
                        String ingredient = String.valueOf(taskEditText.getText());
                        if (!ingredient.isEmpty()) {
                            presenter.sendIngredientToServer(ingredient);
                        } else {
                            Toast.makeText(getContext(), R.string.new_ingredient_must_contain_name, Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create();
            dialog.show();
        });
    }

    @Override
    public void onAddedNewDish() {
        Toast.makeText(getContext(), R.string.added_new_dish, Toast.LENGTH_LONG).show();
        Intent i = new Intent(getContext(), DishesListActivity.class);
        getContext().startActivity(i);
    }

    @Override
    public void setupPhotoPickerListener() {
        fragmentAddNewMealBinding.mealNameImg.setOnClickListener(view -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        });
    }

    @Override
    public void showFormError(@NonNull int errorCode) {
        if (errorCode == WRONG_DISH_NAME) {
            fragmentAddNewMealBinding.mealNameEt.setError(getString(R.string.you_didnt_type_dish_name));
        }
        if (errorCode == WRONG_DISH_DESCRIPTION) {
            fragmentAddNewMealBinding.mealDescriptionTv.setError(getString(R.string.you_didnt_type_dish_description));
        }
        if (errorCode == ERROR_WHEN_ULOAD_PHOTO) {
            Toast.makeText(getContext(), R.string.error_when_uploading_files, Toast.LENGTH_LONG).show();
        }
        if (errorCode == DIDNT_CHOOSE_INGREDIENTS) {
            Toast.makeText(getContext(), R.string.you_didnt_choose_dish_picture, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setupSpinner() {
        presenter.gotIngredientsFromService();
    }

    @Override
    public void setPicture(Bitmap img) {
        fragmentAddNewMealBinding.mealNameImg.setImageBitmap(img);
        selectedImage = img;
    }
}
