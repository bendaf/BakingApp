package com.bendaf.bakingapp;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
            if(mStep.getVideoURL().equals("") && mStep.getThumbnailURL().equals("")) {
                mBinding.playerStep.setVisibility(View.GONE);
            } else {
                mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),
                        new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter())));
                mBinding.playerStep.setPlayer(mPlayer);
                Uri videoUrl = Uri.parse(mStep.getVideoURL().equals("") ? mStep.getThumbnailURL() : mStep.getVideoURL());
                MediaSource mediaSource = new ExtractorMediaSource.Factory(
                        new DefaultHttpDataSourceFactory("UrlGetter")).createMediaSource(videoUrl);

                mPlayer.prepare(mediaSource);
                mPlayer.setPlayWhenReady(true);
            }
        }

        return mBinding.getRoot();
    }

    @Override public void onDestroyView() {
        if(mPlayer != null) mPlayer.release();
        super.onDestroyView();
    }
}
