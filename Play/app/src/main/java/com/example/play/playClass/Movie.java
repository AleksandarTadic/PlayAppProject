package com.example.play.playClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {
    private int id;
    private String title;
    private String description;
    private int year;
    private String videoUrl;
    private String imageUrl;

    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static Movie fromJson(JSONObject json) {
        Movie movie = new Movie();
        try {
            if(json.has("id")) {
                movie.setId(json.getInt("id"));
            }
            if(json.has("title")) {
                movie.setTitle(json.getString("title"));
            }
            if(json.has("description")) {
                movie.setDescription(json.getString("description"));
            }
            if(json.has("year")) {
                movie.setYear(json.getInt("year"));
            }
            if(json.has("video_url")) {
                movie.setVideoUrl(json.getString("video_url"));
            }
            if(json.has("image_url")) {
                movie.setImageUrl(json.getString("image_url"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }
    public Movie fromJsonObject(JSONObject json) {
        Movie movie = new Movie();
        try {
            if(json.has("id")) {
                movie.setId(json.getInt("id"));
            }
            if(json.has("title")) {
                movie.setTitle(json.getString("title"));
            }
            if(json.has("description")) {
                movie.setDescription(json.getString("description"));
            }
            if(json.has("year")) {
                movie.setYear(json.getInt("year"));
            }
            if(json.has("video_url")) {
                movie.setVideoUrl(json.getString("video_url"));
            }
            if(json.has("image_url")) {
                movie.setImageUrl(json.getString("image_url"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }

    public static ArrayList<Movie> fromJsonArray(JSONArray array) {
        ArrayList<Movie> movies = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            try {
                movies.add(Movie.fromJson(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movies;
    }

    public static JSONObject toJson(Movie movie) {
        JSONObject object = new JSONObject();
        try {
            object.put("id", movie.getId());
            object.put("title", movie.getTitle());
            object.put("description", movie.getDescription());
            object.put("year", movie.getYear());
            object.put("video_url", movie.getVideoUrl());
            object.put("image_url", movie.getVideoUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

}
