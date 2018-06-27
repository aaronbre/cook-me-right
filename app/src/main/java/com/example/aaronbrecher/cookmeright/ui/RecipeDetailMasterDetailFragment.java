package com.example.aaronbrecher.cookmeright.ui;

import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.models.Step;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import static com.example.aaronbrecher.cookmeright.ui.StepDetailActivity.FRAGMENT_ARGS_RECIPE_NAME;
import static com.example.aaronbrecher.cookmeright.ui.StepDetailActivity.FRAGMENT_ARGS_STEP_LIST;

public class RecipeDetailMasterDetailFragment extends Fragment implements Player.EventListener {
    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private Button mPreviousButton;
    private Button mNextButton;
    private MediaSession mMediaSession;

    public void setStep(Step step) {
        mStep = step;
    }

    private Step mStep;
    private List<Step> mStepList;
    private String mRecipeName;
    private TextView mInstructionsHeading;
    private TextView mInstructions;

    public RecipeDetailMasterDetailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_detail, container, false);
        mExoPlayerView = rootView.findViewById(R.id.player_view);
        mPreviousButton = rootView.findViewById(R.id.step_detail_previous);
        mNextButton = rootView.findViewById(R.id.step_detail_next);
        mInstructions = rootView.findViewById(R.id.step_detail_instructions);
        mInstructionsHeading = rootView.findViewById(R.id.step_detail_instructions_header);
        mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.video_icon));

        mStep = getArguments().getParcelable(StepDetailActivity.FRAGMENT_ARGS_STEP);
        mStepList = getArguments().getParcelableArrayList(FRAGMENT_ARGS_STEP_LIST);
        mRecipeName = getArguments().getString(FRAGMENT_ARGS_RECIPE_NAME);
        initializeMediaSession();
        initializeExoPlayer();
        updateUiAndPlayer();

        //set a listener on next and previous button to load in the correct step
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button button = (Button) view;
                if(button.getText().toString().equals(getString(R.string.button_next))){
                    mStep = mStepList.get(mStep.getId()+1);
                }else{
                    mStep = mStepList.get(mStep.getId()-1);
                }
                updateUiAndPlayer();
            }
        };
        mPreviousButton.setOnClickListener(listener);
        mNextButton.setOnClickListener(listener);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    //initial setup on the Exoplayer
    private void initializeExoPlayer() {
        if(mExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            mExoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
        }
    }

    private void initializeMediaSession() {

    }

    //set up the text view an player with the data from the step
    //also update the title of the activity to show the recipe name and step num
    public void updateUiAndPlayer() {
        mInstructionsHeading.setText(mStep.getShortDescription());
        mInstructions.setText(mStep.getDescription());
        disableIllegalButton();
        getActivity().setTitle(mRecipeName + " - Step " + (mStep.getId()+1));
        setPlayerToNewMediaSource();
    }

    //if user is at the first step disable the previous button if last disable the next button
    private void disableIllegalButton() {
        if(mStep.getId() == 0){
            mPreviousButton.setEnabled(false);
        }else{
            mPreviousButton.setEnabled(true);
        }if(mStep.getId() == mStepList.size()-1){
            mNextButton.setEnabled(false);
        }else {
            mNextButton.setEnabled(true);
        }
    }

    //set the player to the new media source based on the step data
    private void setPlayerToNewMediaSource() {
        mExoPlayer.stop();
        String userAgent = Util.getUserAgent(getActivity(), "CookMeRight");
        String uriAsString = mStep.getVideoURL();
        //if there is no associated video remove the previous video from the player
        if(uriAsString == null || uriAsString.isEmpty()){
            mExoPlayer.prepare(null);
            return;
        }
        Uri movieUri = Uri.parse(uriAsString);
        MediaSource mediaSource = new ExtractorMediaSource(movieUri, new DefaultDataSourceFactory(getActivity(), userAgent),
                new DefaultExtractorsFactory(),null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    private void releasePlayer(){
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
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

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

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

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * end exoplayer listener functions
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
}
