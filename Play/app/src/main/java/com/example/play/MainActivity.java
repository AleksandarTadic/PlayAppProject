package com.example.play;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.play.playClass.Movie;
import com.example.play.serverCommunication.AsyncServerApi;
import com.example.play.serverCommunication.ReadDataHandler;
import com.example.play.serverCommunication.ServerPath;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText search;
    private ImageView searchButton;
    private ImageView accountDetailsMainButton;
    private ProgressBar progressBar;
    private LinearLayout mainScrollView;

    public ArrayList<Movie> movies = new ArrayList<Movie>();

    public void addMovieArrayObject(Movie rel) {
        this.movies.add(rel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initComponents();
        initMovies();
        initSearchButton();
    }

    public void initComponents() {
        search = findViewById(R.id.searchMovieMain);
        searchButton = findViewById(R.id.searchIconMain);
        progressBar = findViewById(R.id.progressBarMain);
        mainScrollView = findViewById(R.id.scrollviewMainMovies);
        accountDetailsMainButton = findViewById(R.id.accountDetailsMainButton);
        accountDetailsMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AccountDetails.class));
            }
        });
    }

    public void initSearchButton() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movies.clear();
                String searching = search.getText().toString();
                if(searching.length() < 1) {
                    initMovies();
                } else {
                    moviesSearch(searching);
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    public void moviesSearch(String searching) {
        String s = search.getText().toString();
        AsyncServerApi.readFromServer( ServerPath.getServerPath() + "api/movie/search/" + searching, progressBar, new ReadDataHandler(){
            @Override
            public void handleMessage(Message msg) {
                String response = getJson();
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    Movie movie = new Movie();
                    for(int i = 0; i < jsonArray.length(); i++) {
                        System.out.println(jsonArray.getJSONObject(i));
                        addMovieArrayObject(movie.fromJson(jsonArray.getJSONObject(i)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                generateData();
            }
        });
    }


    @SuppressLint("HandlerLeak")
    public void initMovies() {
        AsyncServerApi.readFromServer( ServerPath.getServerPath() + "api/movie", progressBar, new ReadDataHandler(){
            @Override
            public void handleMessage(Message msg) {
                String response = getJson();
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    Movie movie = new Movie();
                    for(int i = 0; i < jsonArray.length(); i++) {
                        System.out.println(jsonArray.getJSONObject(i));
                        addMovieArrayObject(movie.fromJson(jsonArray.getJSONObject(i)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                generateData();
            }
        });
    }

    private void generateData() {
        mainScrollView.removeAllViews();
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ConstraintLayout constraintLayout;
        ImageView coverImage;
        TextView title;
        for(final Movie m : movies) {
            ConstraintLayout item = (ConstraintLayout) inflater.inflate(R.layout.movie_layout, null);

            coverImage = item.findViewById(R.id.imageViewMain);
            Picasso.with(this).load(m.getImageUrl()).fit().centerCrop().into(coverImage);
            title = item.findViewById(R.id.titleLabel);
            title.setText(m.getTitle());

            constraintLayout = item.findViewById(R.id.conLayout);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MoviePlayerActivity.class);
                    intent.putExtra("movie", m);
                    startActivity(intent);
                }
            });
            mainScrollView.addView(item);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

}