package com.world.jst.android.bakingapp.network;

import com.world.jst.android.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipesAPI {

    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    String RECIPES_PATH = "/topher/2017/May/59121517_baking/baking.json";

    // https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
    @GET(RECIPES_PATH)
    Call<List<Recipe>> getRecipes();

}
