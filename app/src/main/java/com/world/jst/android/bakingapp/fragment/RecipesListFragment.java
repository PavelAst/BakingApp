package com.world.jst.android.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.adapter.RecipeRecyclerViewAdapter;
import com.world.jst.android.bakingapp.model.Recipe;
import com.world.jst.android.bakingapp.network.RecipesAPI;
import com.world.jst.android.bakingapp.utils.NetworkHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesListFragment extends Fragment
        implements RecipeRecyclerViewAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.empty_recipes_list_tv)
    TextView mEmptyRecipesListTextView;
    @BindView(R.id.recipes_rv)
    RecyclerView mRecipeRecyclerView;
    @BindView(R.id.loading_indicator_pb)
    ProgressBar mLoadingIndicator;
    private Unbinder mUnbinder;

    // Turn logging on or off
    private static final boolean L = true;
    private static final String TAG = "RecipesListFragment";
    private Call<List<Recipe>> mRecipeCall;
    private RecipeRecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        int numberOfColumns = 1;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        mRecipeRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecipeRecyclerViewAdapter(getActivity(), this);
        mRecipeRecyclerView.setAdapter(mAdapter);

        boolean online = NetworkHelper.hasNetworkAccess(getActivity());
        if (online) {
            loadRecipes();
        } else {
            showErrorMessage(R.string.error_message_network);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        if (mRecipeCall != null) {
            mRecipeCall.cancel();
        }
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void showRecipes() {
        mEmptyRecipesListTextView.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(int resid) {
        /* First, hide the currently visible data */
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mEmptyRecipesListTextView.setVisibility(View.VISIBLE);
        mEmptyRecipesListTextView.setText(resid);
    }

    private void loadRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecipesAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRecipeCall = retrofit.create(RecipesAPI.class).getRecipes();
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecipeCall.enqueue(recipesCallback);
    }

    Callback<List<Recipe>> recipesCallback = new Callback<List<Recipe>>() {
        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
            if (response.isSuccessful()) {
                List<Recipe> recipes = response.body();

                if (recipes != null) {
                    mAdapter.setRecipes(recipes);
                    callCompleted();
                    showRecipes();
                }
            } else {
                callCompleted();
                showErrorMessage(R.string.error_message_all);
                if (L) Log.d(TAG, call.request().url() + " failed: HTTP " + response.code());
            }
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
            showErrorMessage(R.string.error_message_all);
            if (L) Log.e(TAG, call.request().url() + " failed: " + t.toString());
            callCompleted();
        }
    };

    private void callCompleted() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecipeCall = null;
    }

    @Override
    public void onClick(Recipe recipe) {
        Toast.makeText(getActivity(), recipe.toString(), Toast.LENGTH_SHORT).show();
    }
}


