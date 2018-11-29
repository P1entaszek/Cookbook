package com.jaszczurowskip.cookbook.features.addnewmeal;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.databinding.FragmentAddNewMealBinding;
import com.jaszczurowskip.cookbook.datasource.model.IngredientApiModel;
import com.jaszczurowskip.cookbook.features.mealdetails.IngredientsDetailsGridAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewMealFragment extends Fragment {
    private static int RESULT_LOAD_IMG = 0;
    private FragmentAddNewMealBinding fragmentAddNewMealBinding;
    private String[] list = {"egg", "bear", "salad", "tomatoes"};

    public AddNewMealFragment() {
        // Required empty public constructor
    }

    public static AddNewMealFragment newInstance() {
        return new AddNewMealFragment();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IngredientsGridAdapter adapter = new IngredientsGridAdapter(getContext(), list);
        fragmentAddNewMealBinding.ingredientsGridview.setAdapter(adapter);
        photoPickerListener();
        ingredientsSpinner();
        fragmentAddNewMealBinding.mealNameTv.setText("some meal name mate!");
    }

    private void ingredientsSpinner() {
        fragmentAddNewMealBinding.ingredientsSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "Test",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void photoPickerListener() {
        fragmentAddNewMealBinding.mealNameImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                fragmentAddNewMealBinding.mealNameImg.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Błąd podczas wczytywana pliku", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getContext(), "Nie wybrano zdjęcia",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAddNewMealBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_meal, container, false);
        return fragmentAddNewMealBinding.getRoot();
    }
}
