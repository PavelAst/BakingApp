package com.world.jst.android.bakingapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.model.Recipe;
import com.world.jst.android.bakingapp.network.RecipesAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // Turn logging on or off
    private static final boolean L = true;

    private static final String TAG = "RecipesMainActivity";

    private RecipesAPI mRecipesAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadRecipes();
    }

    private void createRecipesAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecipesAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRecipesAPI = retrofit.create(RecipesAPI.class);
    }

    private void loadRecipes() {
        createRecipesAPI();
        mRecipesAPI.getRecipes().enqueue(recipesCallback);
    }

    Callback<List<Recipe>> recipesCallback = new Callback<List<Recipe>>() {
        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
            if (response.isSuccessful()) {
                List<Recipe> recipes = response.body();

                if (recipes != null) {
                    for (Recipe recipe: recipes) {
                        Log.d(TAG, recipe.toString() + "\n");
                    }
                }
            } else {
                if (L) Log.d(TAG, call.request().url() + " failed: HTTP " + response.code());
            }
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
            t.printStackTrace();
            if (L) Log.e(TAG, call.request().url() + " failed: " + t.toString());
        }
    };

}

