package com.world.jst.android.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.adapter.RecipeRecyclerViewAdapter;
import com.world.jst.android.bakingapp.model.Ingredient;
import com.world.jst.android.bakingapp.model.Recipe;
import com.world.jst.android.bakingapp.model.Step;
import com.world.jst.android.bakingapp.network.RecipesAPI;
import com.world.jst.android.bakingapp.utils.NetworkHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesListFragment extends Fragment {

    public interface RecipeOnClickHandler {
        void onClick(Recipe recipe);
    }

    @BindView(R.id.empty_recipes_list_tv)
    TextView mEmptyRecipesListTextView;
    @BindView(R.id.recipes_rv)
    RecyclerView mRecipeRecyclerView;
    @BindView(R.id.loading_indicator_pb)
    ProgressBar mLoadingIndicator;
    private Unbinder mUnbinder;

    // Turn logging on or off
    private static final boolean L = false;
    private static final String TAG = "RecipesListFragment";
    private static final int COLUMN_WIDTH = 300;
    private int mFirstVisibleItemPosition = 0;
    private Call<List<Recipe>> mRecipeCall;
    private RecipeRecyclerViewAdapter mAdapter;
    private RecipeOnClickHandler mCallbacks;
    private Realm mRealm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (L) Log.d(TAG, "*** RecipesListFragment - onCreate ***");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (RecipeOnClickHandler) context;
        if (L) Log.d(TAG, "*** RecipesListFragment - onAttach ***");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (L) Log.d(TAG, "*** RecipesListFragment - onCreateView ***");
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mRealm = Realm.getDefaultInstance();
        RealmResults<Recipe> recipes = mRealm.where(Recipe.class).sort("mId").findAll();
        if (L) Log.d(TAG, "*** Total recipes: " + recipes.size());

        int numberOfColumns = 1;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        mRecipeRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new RecipeRecyclerViewAdapter(getActivity(), recipes, mCallbacks);
        mRecipeRecyclerView.setAdapter(mAdapter);
        if (mAdapter.getItemCount() > 0) {
            showRecipes();
        }

        mRecipeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                mFirstVisibleItemPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
            }
        });

        mRecipeRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        float columnWidthInPixels = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                COLUMN_WIDTH,
                                getActivity().getResources().getDisplayMetrics());
                        int width = mRecipeRecyclerView.getWidth();
                        int columnNumber = Math.round(width / columnWidthInPixels);
                        mRecipeRecyclerView.setLayoutManager(
                                new GridLayoutManager(getActivity(), columnNumber));
                        mRecipeRecyclerView.scrollToPosition(mFirstVisibleItemPosition);
                        mRecipeRecyclerView
                                .getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);
                    }
                });


        boolean online = NetworkHelper.hasNetworkAccess(getActivity());
        if (online) {
            loadRecipes();
        } else if (mAdapter.getItemCount() == 0) {
            showMessage(R.string.error_message_network);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        if (L) Log.d(TAG, "*** RecipesListFragment - onDestroyView ***");
        if (mRecipeCall != null) {
            mRecipeCall.cancel();
        }
        if (mRealm != null) {
            mRealm.close();
        }
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDetach() {
        if (L) Log.d(TAG, "*** RecipesListFragment - onDetach ***");
        super.onDetach();
        mCallbacks = null;
    }

    private void showRecipes() {
        mEmptyRecipesListTextView.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showMessage(int resid) {
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
                    saveToRealm(recipes);
                    callCompleted();
                    showRecipes();
                }
            } else {
                callCompleted();
                if (mAdapter.getItemCount() == 0) {
                    showMessage(R.string.error_message_all);
                }
                if (L) Log.d(TAG, call.request().url() + " failed: HTTP " + response.code());
            }
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
            if (mAdapter.getItemCount() == 0) {
                showMessage(R.string.error_message_all);
            }
            if (L) Log.e(TAG, call.request().url() + " failed: " + t.toString());
            callCompleted();
        }
    };

    private void callCompleted() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecipeCall = null;
    }

    private void saveToRealm(final List<Recipe> retrievedRecipes) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(Ingredient.class);
                realm.delete(Step.class);
                realm.insertOrUpdate(retrievedRecipes);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.

            }
        });
    }
}


