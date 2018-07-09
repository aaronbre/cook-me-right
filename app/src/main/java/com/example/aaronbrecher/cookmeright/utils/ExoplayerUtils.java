package com.example.aaronbrecher.cookmeright.utils;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoplayerUtils {
    private static final String MEDIA_SESSION_TAG = "cookMeRightMediaSession";

    public static SimpleExoPlayer initializeExoPlayer(Context context, Player.EventListener listener) {
        TrackSelector trackSelector = new DefaultTrackSelector();
        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
        exoPlayer.addListener(listener);
        return exoPlayer;
    }

    public static void setPlayerToNewMediaSource(SimpleExoPlayer exoPlayer, Context context, String uriString, String secondaryUri, long position, boolean isPlay) {
        String userAgent = Util.getUserAgent(context, "CookMeRight");
        //if there is no associated video remove the previous video from the player
        if (uriString == null || uriString.isEmpty()) {
            if (secondaryUri == null || secondaryUri.isEmpty()) {
                exoPlayer.prepare(null);
                return;
            } else {
                uriString = secondaryUri;
            }
        }
        Uri movieUri = Uri.parse(uriString);
        MediaSource mediaSource = new ExtractorMediaSource(movieUri, new DefaultDataSourceFactory(context, userAgent),
                new DefaultExtractorsFactory(), null, null);
        exoPlayer.prepare(mediaSource);
        if (position != 0) exoPlayer.seekTo(position);
        exoPlayer.setPlayWhenReady(isPlay);
    }

}
