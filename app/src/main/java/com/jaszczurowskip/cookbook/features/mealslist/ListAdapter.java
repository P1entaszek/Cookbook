package com.jaszczurowskip.cookbook.features.mealslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.features.mealdetails.MealDetailsActivity;
import com.jaszczurowskip.cookbook.utils.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jaszczurowskip on 15.11.2018
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomView> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<DishesApiModel> listOfData;
    private static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    public ListAdapter(Context context, List<DishesApiModel> listOfData) {
        this.context = context;
        this.listOfData = listOfData;
    }

    @NonNull

    @Override
    public ListAdapter.CustomView onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View v = layoutInflater.inflate(R.layout.item_recycler_list, parent, false);
        return new CustomView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.CustomView customView, int position) {
        DishesApiModel currentItem = listOfData.get(position);
        Glide.with(context).load(currentItem.getPicture()).into(customView.mealImage);
        customView.mealName.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return listOfData.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView mealImage;
        TextView mealName;
        ConstraintLayout constraintLayout;

        CustomView(final View itemView) {
            super(itemView);
            this.mealImage = itemView.findViewById(R.id.imgv_list_item);
            this.mealName = itemView.findViewById(R.id.meal_name_tv);
            this.constraintLayout = itemView.findViewById(R.id.root_list_item);
            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            startDetailActivity(this.getAdapterPosition());
        }
    }

    public void startDetailActivity(int position) {
        Intent i = new Intent(context, MealDetailsActivity.class);
        Gson gson = new Gson();
        String dishJSon = gson.toJson(listOfData.get(position));
        i.putExtra(EXTRA_ITEM_ID, dishJSon);
        context.startActivity(i);
    }
}
