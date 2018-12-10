package com.jaszczurowskip.cookbook.features.addnewdish;


import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
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
import com.jaszczurowskip.cookbook.datasource.CookbookClient;
import com.jaszczurowskip.cookbook.datasource.ServerResponseListener;
import com.jaszczurowskip.cookbook.datasource.model.ApiError;
import com.jaszczurowskip.cookbook.datasource.model.DishModelToPost;
import com.jaszczurowskip.cookbook.datasource.model.IngredientApiModel;
import com.jaszczurowskip.cookbook.features.IngredientsRecyclerAdapter;
import com.jaszczurowskip.cookbook.features.disheslist.DishesListActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewDishFragment extends Fragment {
    private static int RESULT_LOAD_IMG = 0;
    private static String ADD_NEW_MEAL_FRAGMENT = "ADD_NEW_MEAL_FRAGMENT";
    private static String typeOfImage = "data:image/jpeg;base64,";
    private FragmentAddNewMealBinding fragmentAddNewMealBinding;
    private ArrayList<String> listSpinnerItems = new ArrayList<>();
    private LinkedHashSet<String> addingNewIngredientToDishHashSet = new LinkedHashSet<>();
    private ArrayList<IngredientApiModel> ingredientArrayList = new ArrayList<>();
    private ArrayList<IngredientApiModel> choosenIngredients = new ArrayList<>();
    private Bitmap selectedImage;
    private IngredientsRecyclerAdapter ingredientsRecyclerAdapter;
    private ArrayAdapter<String> spinnerAdapter;


    public AddNewDishFragment() {
        // Required empty public constructor
    }

    public static AddNewDishFragment newInstance() {
        return new AddNewDishFragment();
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        fetchIngredientsFromRemoteWithSpinner();
        photoPickerListener();
        addNewIngredientToSpinner();
        addNewIngredientToDish();
        sendNewDishToRemote();
    }

    public boolean validatePostNewDish(DishModelToPost dishModelToPost) {
        boolean valid = true;
        String generalToast = getString(R.string.wrong_filled_dish_form);
        String newLine = getString(R.string.new_line);
        if (dishModelToPost.getIngredientIds().isEmpty()) {
            generalToast = generalToast.concat(newLine).concat(getString(R.string.you_didnt_choose_dish_ingredients));
            valid = false;
        }
        if (dishModelToPost.getName().isEmpty()) {
            generalToast = generalToast.concat(newLine).concat(getString(R.string.you_didnt_type_dish_name));
            fragmentAddNewMealBinding.mealNameEt.setError(getString(R.string.you_didnt_type_dish_name));
            valid = false;
        }
        if (dishModelToPost.getRecipe().isEmpty()) {
            generalToast = generalToast.concat(newLine).concat(getString(R.string.you_didnt_type_dish_description));
            fragmentAddNewMealBinding.mealDescriptionTv.setError(getString(R.string.you_didnt_type_dish_description));
            valid = false;
        }
        if (dishModelToPost.getPicture() == null) {
            generalToast = generalToast.concat(newLine).concat(getString(R.string.you_didnt_choose_dish_picture));
            valid = false;
        }
        if (!valid) Toast.makeText(getContext(), generalToast, Toast.LENGTH_LONG).show();
        return valid;
    }

    public void sendNewDishToRemote() {
        fragmentAddNewMealBinding.acceptAddingMealBttn.setOnClickListener(v -> {
            if (validatePostNewDish(preparedNewDishToPost())) {
                CookbookClient.getCookbookClient().sendDishToServer(preparedNewDishToPost(), new ServerResponseListener<DishModelToPost>() {
                    @Override
                    public void onSuccess(DishModelToPost response) {
                        Toast.makeText(getContext(), R.string.added_new_dish, Toast.LENGTH_LONG).show();
                        clearForm();
                        startListActivity();
                    }

                    @Override
                    public void onError(ApiError error) {
                        Toast.makeText(getContext(), R.string.please_check_your_internet_connection, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void startListActivity() {
        Intent i = new Intent(getContext(), DishesListActivity.class);
        getContext().startActivity(i);
    }

    private DishModelToPost preparedNewDishToPost() {
        String preparedImageToPost = null;
        if (selectedImage != null) {
            preparedImageToPost = typeOfImage.concat(encodeToBase64(selectedImage, Bitmap.CompressFormat.JPEG, 20));
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

    private void initView() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        fragmentAddNewMealBinding.ingredientsRv.setLayoutManager(layoutManager);
    }

    private void fetchIngredientsFromRemoteWithSpinner() {
        CookbookClient.getCookbookClient().getAllIngredients(new ServerResponseListener<List<IngredientApiModel>>() {
            @Override
            public void onSuccess(List<IngredientApiModel> ingredients) {
                listSpinnerItems.clear();
                listSpinnerItems.add(getString(R.string.choose_some_ingredients));
                for (int i = 0; i < ingredients.size(); i++) {
                    listSpinnerItems.add(ingredients.get(i).getName());
                    ingredientArrayList.add(ingredients.get(i));
                }
                spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listSpinnerItems);
                fragmentAddNewMealBinding.ingredientsSpinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onError(ApiError error) {
                Log.d(ADD_NEW_MEAL_FRAGMENT, error.getMessage());
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearForm() {
        choosenIngredients.clear();
        addingNewIngredientToDishHashSet.clear();
        ingredientsRecyclerAdapter = new IngredientsRecyclerAdapter(getContext(), choosenIngredients);
        fragmentAddNewMealBinding.ingredientsRv.setAdapter(ingredientsRecyclerAdapter);
        fragmentAddNewMealBinding.mealDescriptionTv.setText("");
        fragmentAddNewMealBinding.mealNameEt.setText("");
        fragmentAddNewMealBinding.mealNameImg.setImageDrawable(getResources().getDrawable(R.drawable.select_photo));
        fragmentAddNewMealBinding.ingredientsSpinner.setAdapter(spinnerAdapter);
    }

    private void addNewIngredientToSpinner() {
        fragmentAddNewMealBinding.addIngredientsBttn.setOnClickListener(v -> {
            final EditText taskEditText = new EditText(getContext());
            final AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.add_new_ingredient))
                    .setView(taskEditText)
                    .setPositiveButton(R.string.add, (dialog1, which) -> {
                        String ingredient = String.valueOf(taskEditText.getText());
                        if (!ingredient.isEmpty()) {
                            CookbookClient.getCookbookClient().sendIngredientToServer(ingredient, new ServerResponseListener<String>() {
                                @Override
                                public void onSuccess(String response) {
                                    Toast.makeText(getContext(), R.string.ingredient_added_to_server, Toast.LENGTH_LONG).show();
                                    fetchIngredientsFromRemoteWithSpinner();
                                }

                                @Override
                                public void onError(ApiError error) {
                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {
                            Toast.makeText(getContext(), R.string.new_ingredient_must_contain_name, Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .create();
            dialog.show();
        });
    }

    private void addNewIngredientToDish() {
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
                Toast.makeText(getContext(), R.string.error_when_uploading_files, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getContext(), R.string.you_didnt_choose_dish_picture, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAddNewMealBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_meal, container, false);
        return fragmentAddNewMealBinding.getRoot();
    }
}
