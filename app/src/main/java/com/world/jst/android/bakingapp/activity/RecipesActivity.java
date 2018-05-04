package com.world.jst.android.bakingapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.fragment.RecipesListFragment;
import com.world.jst.android.bakingapp.model.Recipe;
import com.world.jst.android.bakingapp.network.RecipesAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RecipesListFragment();
    }

}

