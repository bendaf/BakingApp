package com.bendaf.bakingapp;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bendaf.bakingapp.Model.Ingredient;
import com.bendaf.bakingapp.Model.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bendaf on 2018. 08. 20. BakingApp.
 */
public class IngredientsWidget extends RemoteViewsService {

    @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsWidgetFactory();
    }

    class IngredientsWidgetFactory implements RemoteViewsService.RemoteViewsFactory {
        private List<Ingredient> mIngredients = new ArrayList<>();

        @Override public void onCreate() {
        }

        @Override public void onDataSetChanged() {
            String recipeJson =
                    getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE).getString(MainActivity.PREFS_RECIPE, "");
            mIngredients = new Gson().fromJson(recipeJson, Recipe.class).getIngredients();
        }

        @Override public void onDestroy() {
            mIngredients.clear();
        }

        @Override public int getCount() {
            return mIngredients.size();
        }

        @Override public RemoteViews getViewAt(int i) {
            RemoteViews rv = new RemoteViews(getPackageName(), android.R.layout.simple_list_item_1);
            rv.setTextViewText(android.R.id.text1, mIngredients.get(i).getIngredient());
            return rv;
        }

        @Override public RemoteViews getLoadingView() {
            return null;
        }

        @Override public int getViewTypeCount() {
            return 1;
        }

        @Override public long getItemId(int i) {
            return i;
        }

        @Override public boolean hasStableIds() {
            return true;
        }
    }
}