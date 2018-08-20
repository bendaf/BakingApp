package com.bendaf.bakingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bendaf.bakingapp.Model.Recipe;
import com.bendaf.bakingapp.databinding.ActivityMainBinding;
import com.bendaf.bakingapp.databinding.SimpleListItemBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    // Used to access json url page and get response data.
    private static OkHttpClient okHttpClient = new OkHttpClient();

    private ActivityMainBinding mBinding;
    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    private RecipeAdapter mAdapter;
    public static final String EXTRA_RECIPE = "recipe extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Request request = new Request.Builder().url(RECIPES_URL).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Snackbar.make(mBinding.cordRoot, R.string.error_recipes_download, Snackbar.LENGTH_LONG).show();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        @SuppressWarnings("ConstantConditions")
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        mRecipes.addAll(Arrays.asList(gson.fromJson(jsonString, Recipe[].class)));
                        runOnUiThread(new Runnable() {
                            @Override public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    } catch(NullPointerException | IllegalStateException e) {
                        Snackbar.make(mBinding.cordRoot, R.string.error_recipes_string_null, Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

        mAdapter = new RecipeAdapter(mRecipes);
        mBinding.recRecipes.setAdapter(mAdapter);
    }

    class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeVH> {
        ArrayList<Recipe> mRecipes = new ArrayList<>();

        RecipeAdapter(ArrayList<Recipe> recipes) {
            this.mRecipes = recipes;
        }

        @NonNull @Override
        public RecipeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            SimpleListItemBinding binding = SimpleListItemBinding.inflate(inflater, parent, false);
            return new RecipeVH(binding);
        }

        @Override public void onBindViewHolder(@NonNull RecipeVH holder, int position) {
            Recipe recipe = mRecipes.get(position);
            holder.bind(recipe);
        }

        @Override public int getItemCount() {
            return mRecipes.size();
        }

        class RecipeVH extends RecyclerView.ViewHolder implements View.OnClickListener{
            private final SimpleListItemBinding mRecipeBinding;

            RecipeVH(SimpleListItemBinding binding) {
                super(binding.getRoot());
                this.mRecipeBinding = binding;
            }

            void bind(Recipe recipe) {
                mRecipeBinding.tvName.setText(recipe.getName());
                mRecipeBinding.executePendingBindings();
                mRecipeBinding.cardItem.setOnClickListener(this);
                mRecipeBinding.cardItem.setTag(mRecipes.indexOf(recipe));
            }

            @Override public void onClick(View view) {
                Intent startStepListActivity = new Intent(MainActivity.this, StepListActivity.class);
                startStepListActivity.putExtra(EXTRA_RECIPE, mRecipes.get((Integer) view.getTag()));
                startActivity(startStepListActivity);
            }
        }
    }

}
