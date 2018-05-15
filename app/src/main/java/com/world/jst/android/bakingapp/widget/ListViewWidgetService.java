package com.world.jst.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.model.IngredientParcelable;

import java.util.ArrayList;

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(android.content.Intent intent) {
        Log.d("Widgets", "### In ListViewWidgetService - onGetViewFactory");
        return new AppWidgetListView(this.getApplicationContext(), intent);
    }

}

class AppWidgetListView implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<IngredientParcelable> mIngredients;

    public AppWidgetListView(Context context, Intent intent) {
        mContext = context;
        if (intent.hasExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS)) {
            mIngredients = intent.getParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS);
            Log.d("Widgets", "### In ListViewWidgetService - ingredientParcelables: " + mIngredients.size());
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredients == null) return 0;
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        IngredientParcelable ingredient = mIngredients.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item);

        String q = String.valueOf(ingredient.getQuantity());
        String quantity = !q.contains(".") ? q : q.replaceAll("0*$", "")
                .replaceAll("\\.$", "");

        views.setTextViewText(R.id.w_ingredient_quantity_tv, quantity);
        views.setTextViewText(R.id.w_ingredient_measure_tv, ingredient.getMeasure());
        views.setTextViewText(R.id.w_ingredient_name_tv, ingredient.getName());

//        Intent fillInIntent = new Intent(mContext, RecipeDetailsActivity.class);
//        fillInIntent.putExtra(RecipeDetailsActivity.RECIPE_ITEM_ID, mRecipeId);
//        fillInIntent.putExtra(RecipeDetailsActivity.RECIPE_ITEM_NAME, mRecipeName);
//        views.setOnClickFillInIntent(R.id.widget_item_layout, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
