package com.example.play;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.play.playClass.Movie;
import com.example.play.serverCommunication.ServerPath;
import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MoviePlayerActivity extends AppCompatActivity {
    ImageView backButton;
    TextView title;
    ImageView coverImage;
    JCVideoPlayerStandard videoPlayer;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_movie_player);

        initComponents();
    }

    private void initComponents() {
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        backButton = findViewById(R.id.moviePlayerBackImageButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = findViewById(R.id.moviePlayerTitleLabel);
        title.setText(movie.getTitle() + " (" + movie.getYear() + ")");

        coverImage = findViewById(R.id.moviePlayerCoverImage);
        Picasso.with(MoviePlayerActivity.this).load(movie.getImageUrl()).fit().centerCrop().into(coverImage);

        videoPlayer = (JCVideoPlayerStandard) findViewById(R.id.movieVideoPlayer);
        String uri = ServerPath.getServerPath() + "api/video/" + movie.getId();
        videoPlayer.setUp(uri, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, movie.getTitle() + " (" + movie.getYear() + ")");

        description = findViewById(R.id.moviePlayerDescriptionLabel);
        description.setText(movie.getDescription());

    }

    @Override
    public void onBackPressed() {
        videoPlayer.release();
        videoPlayer = null;
        finish();
        super.onBackPressed();
    }



}