package com.jaszczurowskip.cookbook.features.mealslist;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.features.mealdetails.MealDetailsActivity;
import com.jaszczurowskip.cookbook.utils.Utils;

/**
 * Created by jaszczurowskip on 15.11.2018
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomView> {
    private Context context;
    private LayoutInflater layoutInflater;

    public ListAdapter(Context context) {
        this.context = context;
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

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mealImage;
        TextView mealName;
        ConstraintLayout constraintLayout;

        CustomView(final View itemView) {
            super(itemView);
            this.mealImage = itemView.findViewById(R.id.meal_img);
            this.mealName = itemView.findViewById(R.id.meal_name);
            this.constraintLayout = itemView.findViewById(R.id.root_list_item);
            constraintLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Utils.startDetailActivity(context, MealDetailsActivity.class);
        }
    }

}
