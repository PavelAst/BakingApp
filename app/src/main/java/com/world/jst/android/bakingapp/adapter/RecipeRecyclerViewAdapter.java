package com.world.jst.android.bakingapp.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.fragment.RecipesListFragment;
import com.world.jst.android.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class RecipeRecyclerViewAdapter extends
        RealmRecyclerViewAdapter<Recipe, RecipeRecyclerViewAdapter.RecipeViewHolder> {

    private final RecipesListFragment.RecipeOnClickHandler mClickHandler;

    public RecipeRecyclerViewAdapter(@Nullable OrderedRealmCollection<Recipe> data,
                                     RecipesListFragment.RecipeOnClickHandler clickHandler) {
        super(data, true);
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int position) {
        Recipe recipe = getData().get(position);
        recipeViewHolder.bindTo(recipe);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.recipe_name_tv)
        TextView mRecipeNameTV;
        @BindView(R.id.recipe_poster_iv)
        ImageView mRecipePosterIV;

        public RecipeViewHolder(View recipeView) {
            super(recipeView);
            ButterKnife.bind(this, recipeView);
            recipeView.setOnClickListener(this);
        }

        public void bindTo(Recipe recipe) {
            mRecipeNameTV.setText(recipe.mName);
            if (!TextUtils.isEmpty(recipe.mImage)) {
                Picasso.get()
                        .load(recipe.mImage)
                        .placeholder(R.drawable.cake_placeholder)
                        .error(R.drawable.cake_placeholder)
                        .into(mRecipePosterIV);
            } else {
                Picasso.get()
                        .load(R.drawable.cake_placeholder)
                        .into(mRecipePosterIV);
            }
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = getData().get(adapterPosition);
            if (mClickHandler != null) {
                mClickHandler.onClick(recipe);
            }
        }
    }

}
