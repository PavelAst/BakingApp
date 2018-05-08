package com.world.jst.android.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.adapter.IngredientRecyclerViewAdapter;
import com.world.jst.android.bakingapp.model.Ingredient;
import com.world.jst.android.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

public class RecipeIngredientsFragment extends Fragment {

    @BindView(R.id.recipe_ingredients_rv)
    RecyclerView mRecipeIngredientsRecyclerView;
    private Unbinder mUnbinder;

    private static final String RECIPE_ITEM_ID = "recipe_item_id";
    private int mRecipeId;
    private Recipe mRecipe;

    public static RecipeIngredientsFragment newInstance(int recipeId) {
        RecipeIngredientsFragment fragment = new RecipeIngredientsFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_ITEM_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeIngredientsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipeId = getArguments().getInt(RECIPE_ITEM_ID, 1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Realm realm = Realm.getDefaultInstance();
        try {
            mRecipe = realm.where(Recipe.class)
                    .equalTo("mId", mRecipeId)
                    .findFirst();
        } finally {
            realm.close();
        }

        mRecipeIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        IngredientRecyclerViewAdapter adapter = new IngredientRecyclerViewAdapter(mRecipe.mIngredients);
        mRecipeIngredientsRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
