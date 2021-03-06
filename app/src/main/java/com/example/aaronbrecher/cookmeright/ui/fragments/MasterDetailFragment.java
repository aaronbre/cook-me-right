package com.example.aaronbrecher.cookmeright.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.models.Step;
import com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity;
import com.example.aaronbrecher.cookmeright.ui.StepDetailActivity;
import com.example.aaronbrecher.cookmeright.utils.ExoplayerUtils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import static com.example.aaronbrecher.cookmeright.ui.StepDetailActivity.FRAGMENT_ARGS_RECIPE_NAME;
import static com.example.aaronbrecher.cookmeright.ui.StepDetailActivity.FRAGMENT_ARGS_STEP_LIST;

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
    private long mVideoPosition;
    private List<Step> mStepList;
    private String mRecipeName;
    private TextView mInstructionsHeading;
    private TextView mInstructions;
    private PlaybackStateCompat.Builder mStateBuilder;
    private boolean mPlayWhenReady;

    public void setStep(Step step) {
        mStep = step;
    }

    public void setVideoPosition(long videoPosition) {
        mVideoPosition = videoPosition;
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        mPlayWhenReady = playWhenReady;
    }

    public MasterDetailFragment() {

    }

    //this code is in onResume for because when app is sent to background onCreateView is not called
    //resulting in null pointer exception and other bugs...
    @Override
    public void onResume() {
        super.onResume();
        mExoPlayer = ExoplayerUtils.initializeExoPlayer(mExoPlayer, getActivity(), this);
        mExoPlayerView.setPlayer(mExoPlayer);
        initializeMediaSession();
        updateUiAndPlayer();
        Log.d(TAG, "onResume: called");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION_KEY)) {
            mVideoPosition = savedInstanceState.getLong(POSITION_KEY);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_KEY);
            mStep = savedInstanceState.getParcelable(STEP_KEY);
        }
        Log.d(TAG, "onCreateView: called");
        View rootView = inflater.inflate(R.layout.fragment_master_detail, container, false);
        mExoPlayerView = rootView.findViewById(R.id.player_view);
        mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.video_icon));

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                || getResources().getBoolean(R.bool.isTablet)){
            mPreviousButton = rootView.findViewById(R.id.step_detail_previous);
            mNextButton = rootView.findViewById(R.id.step_detail_next);
            mInstructions = rootView.findViewById(R.id.step_detail_instructions);
            mInstructionsHeading = rootView.findViewById(R.id.step_detail_instructions_header);
            //set a listener on next and previous button to load in the correct step
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button button = (Button) view;
                    if (button.getText().toString().equals(getString(R.string.button_next))) {
                        mStep = mStepList.get(mStep.getId() + 1);
                    } else {
                        mStep = mStepList.get(mStep.getId() - 1);
                    }
                    mVideoPosition = 0;
                    mPlayWhenReady = false;
                    updateUiAndPlayer();
                }
            };
            mPreviousButton.setOnClickListener(listener);
            mNextButton.setOnClickListener(listener);
        }

        if (mStep == null)
            mStep = getArguments().getParcelable(StepDetailActivity.FRAGMENT_ARGS_STEP);
        mStepList = getArguments().getParcelableArrayList(FRAGMENT_ARGS_STEP_LIST);
        mRecipeName = getArguments().getString(FRAGMENT_ARGS_RECIPE_NAME);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(POSITION_KEY, mVideoPosition);
        outState.putBoolean(PLAY_KEY, mPlayWhenReady);
        outState.putParcelable(STEP_KEY, mStep);
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
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ||
                getResources().getBoolean(R.bool.isTablet)){
            mInstructionsHeading.setText(mStep.getShortDescription());
            mInstructions.setText(mStep.getDescription());
            disableIllegalButton();
        }
        getActivity().setTitle(mRecipeName + " - Step " + (mStep.getId() + 1));
        //if it is a tablet need to update the step in activity to properly display in the
        if (getResources().getBoolean(R.bool.isTablet)) ((RecipeDetailActivity) getActivity()).setStep(mStep);
        ExoplayerUtils.setPlayerToNewMediaSource(mExoPlayer, getActivity(), mStep.getVideoURL(), mVideoPosition, mPlayWhenReady);
    }

    //if user is at the first step disable the previous button if last disable the next button
    private void disableIllegalButton() {
        if (mStep.getId() == 0) {
            mPreviousButton.setEnabled(false);
        } else {
            mPreviousButton.setEnabled(true);
        }
        if (mStep.getId() == mStepList.size() - 1) {
            mNextButton.setEnabled(false);
        } else {
            mNextButton.setEnabled(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mVideoPosition = mExoPlayer.getCurrentPosition();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
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
}
