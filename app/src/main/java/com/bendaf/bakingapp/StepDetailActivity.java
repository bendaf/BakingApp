package com.bendaf.bakingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.bendaf.bakingapp.Model.Recipe;
import com.bendaf.bakingapp.Model.Step;
import com.bendaf.bakingapp.databinding.ActivityStepDetailBinding;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

    ActivityStepDetailBinding mBinding;
    Recipe mRecipe;
    private SimpleExoPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);
        if(mBinding.playerStep != null) {

            if(getIntent().hasExtra(StepDetailFragment.EXTRA_STEP)) {
                Step step = getIntent().getParcelableExtra(StepDetailFragment.EXTRA_STEP);
                if(step.getVideoURL().equals("") && step.getThumbnailURL().equals("")) {
                    mBinding.playerStep.setVisibility(View.GONE);
                } else {
                    mPlayer = ExoPlayerFactory.newSimpleInstance(this,
                            new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));
                    mBinding.playerStep.setPlayer(mPlayer);
                    Uri videoUrl = Uri.parse(step.getVideoURL().equals("") ? step.getThumbnailURL() : step.getVideoURL());
                    MediaSource mediaSource = new ExtractorMediaSource.Factory(
                            new DefaultHttpDataSourceFactory("UrlGetter")).createMediaSource(videoUrl);

                    mPlayer.prepare(mediaSource);
                    mPlayer.setPlayWhenReady(true);
                }
            }
        } else {
            setSupportActionBar(mBinding.detailToolbar);

            // Show the Up button in the action bar.
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            // savedInstanceState is non-null when there is fragment state
            // saved from previous configurations of this activity
            // (e.g. when rotating the screen from portrait to landscape).
            // In this case, the fragment will automatically be re-added
            // to its container so we don't need to manually add it.
            // For more information, see the Fragments API guide at:
            //
            // http://developer.android.com/guide/components/fragments.html
            //
            if(savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.step_detail_container, fragment)
                        .commit();
            }
        }
        if(getIntent().hasExtra(MainActivity.EXTRA_RECIPE)) {
            mRecipe = getIntent().getParcelableExtra(MainActivity.EXTRA_RECIPE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent stepListActivity = new Intent(this, StepListActivity.class);
            stepListActivity.putExtra(MainActivity.EXTRA_RECIPE, mRecipe);
            navigateUpTo(stepListActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onStop() {
        if(mPlayer != null) mPlayer.release();
        super.onStop();
    }
}
