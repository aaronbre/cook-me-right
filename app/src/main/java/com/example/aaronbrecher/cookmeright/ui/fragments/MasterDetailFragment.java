package com.example.aaronbrecher.cookmeright.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.ViewModels.RecipeDetailViewModel;
import com.example.aaronbrecher.cookmeright.models.Step;
import com.example.aaronbrecher.cookmeright.utils.ExoplayerUtils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

public class MasterDetailFragment extends Fragment implements Player.EventListener {
    private static final String TAG = MasterDetailFragment.class.getSimpleName();
    private static final String MEDIA_SESSION_TAG = "cookMeRightMediaSession";
    private static final String POSITION_KEY = "video position";
    private static final String PLAY_KEY = "isPlay";
    private static final String STEP_KEY = "step";

    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private Button mPreviousButton;
    private Button mNextButton;
    private MediaSessionCompat mMediaSession;

    private Step mStep;
    private RecipeDetailViewModel mViewModel;
    private TextView mInstructionsHeading;
    private TextView mInstructions;
    private PlaybackStateCompat.Builder mStateBuilder;

    public MasterDetailFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(RecipeDetailViewModel.class);
        mViewModel.getCurrentStepIndex().observe(this, getObserver());
    }

    //this code is in onResume for because when app is sent to background onCreateView is not called
    //resulting in null pointer exception for the exoplayer and other bugs, the code is only run if
    //the app was sent to the background not on orientation change and original load, this is done by
    //checking the viewModel live data, if the activity is just sent to the background the value will
    //not be null.
    @Override
    public void onResume() {
        super.onResume();
        if (mExoPlayer == null && mViewModel.getCurrentStepIndex().getValue() != null) {
            mExoPlayer = ExoplayerUtils.initializeExoPlayer(getActivity(), this);
            mExoPlayerView.setPlayer(mExoPlayer);
            initializeMediaSession();
            updateUiAndPlayer();
        }
        Log.d(TAG, "onResume: called");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_detail, container, false);
        mExoPlayerView = rootView.findViewById(R.id.player_view);
        mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.video_icon));

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                || getResources().getBoolean(R.bool.isTablet)) {
            mPreviousButton = rootView.findViewById(R.id.step_detail_previous);
            mNextButton = rootView.findViewById(R.id.step_detail_next);
            mInstructions = rootView.findViewById(R.id.step_detail_instructions);
            mInstructionsHeading = rootView.findViewById(R.id.step_detail_instructions_header);
            //set a listener on next and previous button to load in the correct step
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button) view;
                    int value;
                    if (button.getText().toString().equals(getString(R.string.button_next))) value = 1;
                    else value = -1;
                    //posting the new value will update the UI etc. via the observer
                    mViewModel.setCurrentStepIndex(mViewModel.getCurrentStepIndex().getValue() + value);
                }
            };
            mPreviousButton.setOnClickListener(listener);
            mNextButton.setOnClickListener(listener);
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.getCurrentStepIndex().removeObservers(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mViewModel.setPreviousIndex(mViewModel.getCurrentStepIndex().getValue());
    }

    private void initializeMediaSession() {
        mMediaSession = new MediaSessionCompat(getActivity(), MEDIA_SESSION_TAG);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackState.ACTION_PLAY |
                        PlaybackState.ACTION_PAUSE |
                        PlaybackState.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackState.ACTION_PLAY_PAUSE);
        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new CookMeSessionCallback());
        mMediaSession.setActive(true);
    }

    //set up the text view an player with the data from the step
    //also update the title of the activity to show the recipe name and step num
    public void updateUiAndPlayer() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ||
                getResources().getBoolean(R.bool.isTablet)) {
            mInstructionsHeading.setText(mStep.getShortDescription());
            mInstructions.setText(mStep.getDescription());
        }
        if (mExoPlayer == null) {
            mExoPlayer = ExoplayerUtils.initializeExoPlayer(getActivity(), this);
            mExoPlayerView.setPlayer(mExoPlayer);
            initializeMediaSession();
        }
        ExoplayerUtils.setPlayerToNewMediaSource(mExoPlayer, getActivity(), mStep.getVideoURL(),
                mStep.getThumbnailURL(), mViewModel.getVideoPosition(), mViewModel.isPlayWhenReady());
    }

    //if user is at the first step disable the previous button if last disable the next button
    private void disableIllegalButton(int index) {
        if (index == 0) {
            mPreviousButton.setEnabled(false);
        } else {
            mPreviousButton.setEnabled(true);
        }
        if (index == mViewModel.getSteps().size() - 1) {
            mNextButton.setEnabled(false);
        } else {
            mNextButton.setEnabled(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mViewModel.setVideoPosition(mExoPlayer.getCurrentPosition());
            mViewModel.setPlayWhenReady(mExoPlayer.getPlayWhenReady());
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * Exoplayer listener functions
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    // update the media session when the exoplayer controls are used
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_READY && playWhenReady) {
            mStateBuilder.setState(PlaybackState.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == Player.STATE_READY) {
            mStateBuilder.setState(PlaybackState.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    public class CookMeSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    public class MediaReceiver extends BroadcastReceiver {
        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * end exoplayer listener functions
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private Observer<Integer> getObserver() {
        return new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                //check if this is being called on orientation change, if so there was no change
                //so do not reset the video position
                Log.d(TAG, "onChanged: called");
                if (mViewModel.getPreviousIndex() != integer) {
                    mViewModel.setPlayWhenReady(false);
                    mViewModel.setVideoPosition(0);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                            || getResources().getBoolean(R.bool.isTablet)) disableIllegalButton(integer);
                }
                mStep = mViewModel.getSteps().get(integer);
                updateUiAndPlayer();
            }
        };
    }
}
