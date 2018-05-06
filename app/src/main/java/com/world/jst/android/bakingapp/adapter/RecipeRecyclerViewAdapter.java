package com.world.jst.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

    private final Context mContext;
    private final RecipesListFragment.RecipeOnClickHandler mClickHandler;

    public RecipeRecyclerViewAdapter(Context context,
                                     @Nullable OrderedRealmCollection<Recipe> data,
                                     RecipesListFragment.RecipeOnClickHandler clickHandler) {
        super(data, true);
        mContext = context;
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
            if (!recipe.mImage.isEmpty()) {
                Picasso.with(mContext)
                        .load(recipe.mImage)
                        .placeholder(R.drawable.cake_default)
                        .error(R.drawable.cake_default)
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
