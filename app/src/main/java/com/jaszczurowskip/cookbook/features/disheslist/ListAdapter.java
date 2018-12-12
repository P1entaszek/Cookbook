package com.jaszczurowskip.cookbook.features.disheslist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.datasource.model.DishesApiModel;
import com.jaszczurowskip.cookbook.features.dishdetails.DishDetailsActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by jaszczurowskip on 15.11.2018
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomView> {
    @NonNull
    private final Context context;
    @Nullable
    private LayoutInflater layoutInflater;
    @NonNull
    private final List<DishesApiModel> listOfData;
    private static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";

    public ListAdapter(final @NonNull Context context, final @NonNull List<DishesApiModel> listOfData) {
        this.context = context;
        this.listOfData = listOfData;
    }

    @NonNull

    @Override
    public ListAdapter.CustomView onCreateViewHolder(final @NonNull ViewGroup parent, final @NonNull int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        View v = layoutInflater.inflate(R.layout.item_recycler_list, parent, false);
        return new CustomView(v);
    }

    @Override
    public void onBindViewHolder(final @NonNull ListAdapter.CustomView customView, final int position) {
        DishesApiModel currentItem = listOfData.get(position);
        Glide.with(context).load(currentItem.getPicture()).into(customView.mealImage);
        customView.mealName.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return listOfData.size();
    }

    class CustomView extends RecyclerView.ViewHolder implements View.OnClickListener {
        @NonNull
        private final CircleImageView mealImage;
        @NonNull
        private final TextView mealName;
        @NonNull
        private final ConstraintLayout constraintLayout;

        CustomView(final @NonNull View itemView) {
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

    private void startDetailActivity(final @NonNull int position) {
        Intent i = new Intent(context, DishDetailsActivity.class);
        long id = listOfData.get(position).getId();
        i.putExtra(EXTRA_ITEM_ID, String.valueOf(id));
        context.startActivity(i);
    }
}
