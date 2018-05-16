package com.world.jst.android.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.adapter.StepRecyclerViewAdapter;
import com.world.jst.android.bakingapp.model.Recipe;
import com.world.jst.android.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

public class RecipeStepsFragment extends Fragment {

    public interface RecipeStepsOnClickHandler {
        void onOptionClick(Step step, int position);
    }

    @BindView(R.id.recipe_steps_rv)
    RecyclerView mRecipeStepsRecyclerView;
    private Unbinder mUnbinder;

    private static final String RECIPE_ITEM_ID = "recipe_item_id";
    private int mRecipeId;
    private Recipe mRecipe;
    private RecipeStepsOnClickHandler mListener;

    public static RecipeStepsFragment newInstance(int recipeId) {
        RecipeStepsFragment fragment = new RecipeStepsFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_ITEM_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    public RecipeStepsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipeId = getArguments().getInt(RECIPE_ITEM_ID, 1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (RecipeStepsOnClickHandler) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Realm realm = Realm.getDefaultInstance();
        try {
            mRecipe = realm.where(Recipe.class)
                    .equalTo("mId", mRecipeId)
                    .findFirst();
        } finally {
            realm.close();
        }

        mRecipeStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        StepRecyclerViewAdapter adapter = new StepRecyclerViewAdapter(mRecipe.mSteps, mListener);
        mRecipeStepsRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
