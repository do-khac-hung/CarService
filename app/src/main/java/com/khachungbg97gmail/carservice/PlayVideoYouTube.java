package com.khachungbg97gmail.carservice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayVideoYouTube extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    YouTubePlayerView youTubePlayerView;
    String id="";
    int REQUEST=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_you_tube);
        youTubePlayerView=(YouTubePlayerView) findViewById(R.id.myYoutube);
        Intent playVideo=getIntent();
        id=playVideo.getStringExtra("idVideoPlay");
        youTubePlayerView.initialize(HowToVideo.API_KEY,this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(id);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
       if(youTubeInitializationResult.isUserRecoverableError()){
           youTubeInitializationResult.getErrorDialog(PlayVideoYouTube.this,REQUEST);
       }
       else{
           Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST){
            youTubePlayerView.initialize(HowToVideo.API_KEY,PlayVideoYouTube.this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
