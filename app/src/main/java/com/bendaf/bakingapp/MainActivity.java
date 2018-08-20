package com.bendaf.bakingapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.bendaf.bakingapp.Model.Recipe;
import com.bendaf.bakingapp.databinding.ActivityMainBinding;
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
                        String jsonString = response.body().string();
                        Gson gson = new Gson();
                        mRecipes = new ArrayList<>(Arrays.asList(gson.fromJson(jsonString, Recipe[].class)));

                    } catch(NullPointerException | IllegalStateException e) {
                        Snackbar.make(mBinding.cordRoot, R.string.error_recipes_string_null, Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}
