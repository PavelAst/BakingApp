package com.world.jst.android.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.model.Ingredient;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class IngredientRecyclerViewAdapter extends
        RealmRecyclerViewAdapter<Ingredient, IngredientRecyclerViewAdapter.IngredientViewHolder> {

    public IngredientRecyclerViewAdapter(@Nullable OrderedRealmCollection<Ingredient> data) {
        super(data, true);
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = getData().get(position);
        holder.bindTo(ingredient);
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_quantity_tv)
        TextView mQuantityTV;
        @BindView(R.id.ingredient_measure_tv)
        TextView mIngredientMeasureTV;
        @BindView(R.id.ingredient_name_tv)
        TextView mIngredientNameTV;

        public IngredientViewHolder(View ingredientView) {
            super(ingredientView);
            ButterKnife.bind(this, ingredientView);
        }

        public void bindTo(Ingredient ingredient) {
            mQuantityTV.setText("" + ingredient.mQuantity);
            mIngredientMeasureTV.setText(ingredient.mMeasure);
            mIngredientNameTV.setText(ingredient.mName);
        }

    }
}

