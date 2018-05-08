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

//    private class IngredientViewHolder extends RecyclerView.ViewHolder {
//
//        private Ingredient mIngredient;
//
//        private TextView mQuantityTV;
//        private TextView mIngredientNameTV;
//
//        public IngredientViewHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(R.layout.recipe_ingredient_item, parent, false));
//
//            mQuantityTV = itemView.findViewById(R.id.ingredient_quantity_tv);
//            mIngredientNameTV =  itemView.findViewById(R.id.ingredient_name_tv);
//        }
//
//        public void bind(Ingredient ingredient) {
//            mIngredient = ingredient;
//            mQuantityTV.setText(mIngredient.mQuantity + " " + mIngredient.mMeasure);
//            mIngredientNameTV.setText(mIngredient.mName);
//        }
//
//    }



//    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
//
//        private List<Crime> mCrimes;
//
//        public CrimeAdapter(List<Crime> crimes) {
//            mCrimes = crimes;
//        }
//
//        @Override
//        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//            return new CrimeHolder(layoutInflater, parent);
//        }
//
//        @Override
//        public void onBindViewHolder(CrimeHolder holder, int position) {
//            Crime crime = mCrimes.get(position);
//            holder.bind(crime);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mCrimes.size();
//        }
//    }

}
