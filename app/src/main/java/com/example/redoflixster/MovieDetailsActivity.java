package com.example.redoflixster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.redoflixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String TAG = "MoviesDetailActivity";
    //The movie I am displaying
    Movie movie;

    //add view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    TextView tvRelease;
    String videoKey;
    ImageView ivImage;

    //new apicall link for movies
    public static final String VIDEOS_API_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        //set all the view objects
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);
        tvRelease = findViewById(R.id.tvRelease);
        ivImage = findViewById(R.id.ivImage);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s' ", movie.getTitle()));

        //set the title and overview and release
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvRelease.setText(movie.getReleaseDate());

        //set the image
        String imageUrl;
        int placeHolderImg;

        imageUrl = movie.getBackdropPath();
        //This will store the placeholder image for landscape
        placeHolderImg = R.drawable.flicks_backdrop_placeholder;
        Glide.with(this).load(imageUrl).transform(new RoundedCorners(20)).placeholder(placeHolderImg).into(ivImage);


        //vote avg from API is from 0-10 we need to convert to 0 -5
        float voteAvg = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAvg = voteAvg > 0 ? voteAvg / 2.0f : voteAvg);

        //get youtube videos
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_API_URL, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results" + results);
                    if(results.length() != 0)
                    {
                        JSONObject trailer = results.getJSONObject(0);
                        videoKey = trailer.getString("key");
                        //When you click on picture go to youtube trailer.
                        ivImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // go to movie trailer activity
                                if(videoKey != null){
                                    Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                                    intent.putExtra("videokeyvalue", videoKey);
                                    startActivity(intent);
                                }
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });

    }
}
