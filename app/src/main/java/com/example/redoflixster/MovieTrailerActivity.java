package com.example.redoflixster;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MovieTrailerActivity extends YouTubeBaseActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_movie_trailer);

        // get the videokeyvalue passed from the MovieDetailActivity through the intent
        final String videoId = getIntent().getStringExtra("videokeyvalue");
        // resolve the player view from the layout
        YouTubePlayerView playerView = findViewById(R.id.player);

        // initialize with API key stored in secret.xml
        playerView.initialize(getString(R.string.youtubekey), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                //do any work here to cue video, play video, ect.
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                //log the error
                Log.e("MovieTrailerActivity", "Error initializing Yoututbe Player");
            }
        });

    }
}
