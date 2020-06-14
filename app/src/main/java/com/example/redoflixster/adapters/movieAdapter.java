package com.example.redoflixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.redoflixster.MovieDetailsActivity;
import com.example.redoflixster.R;
import com.example.redoflixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class movieAdapter extends RecyclerView.Adapter<movieAdapter.ViewHolder> {
    Context context;
    List<Movie> movies;
    public static final String TAG = "MovieAdapter";
    public movieAdapter(Context context, List<Movie> movies){
        this.context = context;
        this.movies = movies;
    }



    // involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
       View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder" + position);
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        //Bind the movie data into the VH
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            //setting itemView's OnClickListener so that we can click on each item
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;

            int placeHolderImg ;
            // if phone is in landscape - imageUrl = backdrop image
            //if in portrait mode - imageUrl = posterPath image
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
                //This will store the placeholder image for landscape
                placeHolderImg = R.drawable.flicks_backdrop_placeholder;
            } else{
                imageUrl = movie.getPosterPath();
                placeHolderImg = R.drawable.flicks_movie_placeholder;
            }
            Glide.with(context).load(imageUrl).transform(new RoundedCorners(20)).placeholder(placeHolderImg).into(ivPoster);
        }

        // When the user clicks on a movie, show the movie details for that movie
        @Override
        public void onClick(View v) {
           // get item position
           int position = getAdapterPosition();
           //make sure the position actually exist in this view
            if(position != RecyclerView.NO_POSITION){
                //get the movie at this position, note this won't work if class is static
                Movie movie = movies.get(position);
                //create intent to go to details page
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                //show the activity
                context.startActivity(intent);
            }
        }
    }
}
