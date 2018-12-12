package com.jaszczurowskip.cookbook.features;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jaszczurowskip.cookbook.R;
import com.jaszczurowskip.cookbook.datasource.model.IngredientApiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaszczurowskip on 27.11.2018
 */
public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientsRecyclerAdapter.ViewHolder> {

    @NonNull
    private final Context context;
    @NonNull
    private final List<IngredientApiModel> list;

    public IngredientsRecyclerAdapter(final @NonNull Context context, final @NonNull List<IngredientApiModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public IngredientsRecyclerAdapter.ViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final @NonNull int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final @NonNull IngredientsRecyclerAdapter.ViewHolder holder, final @NonNull int position) {

        holder.title.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;

        public ViewHolder(final @NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ingredient);
        }
    }
}