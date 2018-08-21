package com.bendaf.bakingapp;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bendaf.bakingapp.Model.Step;
import com.bendaf.bakingapp.databinding.StepDetailBinding;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String EXTRA_STEP = "item_id";
    private static final String PLAYER_POSITION = "player_pos";
    private static final String PLAY_WHEN_REDY = "play_when_ready";

    /**
     * The dummy content this fragment is presenting.
     */
    private Step mStep;
    private SimpleExoPlayer mPlayer;

    StepDetailBinding mBinding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
        if(getArguments().containsKey(EXTRA_STEP)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mStep = getArguments().getParcelable(EXTRA_STEP);

            Activity activity = this.getActivity();
            assert activity != null;
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if(appBarLayout != null) {
                appBarLayout.setTitle(mStep.getShortDescription());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.step_detail, container, false);
        // Show the dummy content as text in a TextView.
        if(mStep != null) {
            mBinding.tvStepInstructions.setText(mStep.getDescription());
        }

        return mBinding.getRoot();
    }

    @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if(mPlayer != null && savedInstanceState != null) {
            long pos = savedInstanceState.getLong(PLAYER_POSITION, 0);
            mPlayer.seekTo(pos);
            mPlayer.setPlayWhenReady(savedInstanceState.getBoolean(PLAY_WHEN_REDY, true));
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override public void onStart() {
        super.onStart();
        if(Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if((Util.SDK_INT <= 23 || mPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        if(mPlayer != null) {
            outState.putLong(PLAYER_POSITION, mPlayer.getCurrentPosition());
            outState.putBoolean(PLAY_WHEN_REDY, mPlayer.getPlayWhenReady());
        }
        super.onSaveInstanceState(outState);
    }

    private void initializePlayer() {
        if(mStep != null) {
            if(mStep.getVideoURL().equals("")) {
                mBinding.playerStep.setVisibility(View.GONE);
                if(!mStep.getThumbnailURL().equals("")) {
                    Picasso.get().load(mStep.getThumbnailURL()).into(mBinding.ivThumbnail);
                    mBinding.ivThumbnail.setVisibility(View.VISIBLE);
                }
            } else {
                mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),
                        new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));
                mBinding.playerStep.setPlayer(mPlayer);
                MediaSource mediaSource = new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("UrlGetter")).createMediaSource(Uri.parse(mStep.getVideoURL()));

                mPlayer.prepare(mediaSource);
                mPlayer.setPlayWhenReady(true);
            }
        }
    }

    public void releasePlayer() {
        if(mPlayer != null) mPlayer.release();
    }

}
