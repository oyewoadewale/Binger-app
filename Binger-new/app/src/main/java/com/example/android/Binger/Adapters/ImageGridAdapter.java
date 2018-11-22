package com.example.android.Binger.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.Binger.Activities.DetailActivity;
import com.example.android.Binger.Objects.Movie;
import com.example.android.Binger.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Adewale on 22/11/2018.
 */

public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.GridViewHolder> {
    String mName;
    String mImage;
    Context mContext;
    Context context1;
    private List<Movie> mMovieList = null;

    private  static final String IMAGE_URL = "http://image.tmdb.org/t/p/w300/";


    public ImageGridAdapter(Context context, List<Movie> movieList){
        mContext = context;
        mMovieList = movieList;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.image_grid;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        GridViewHolder viewHolder = new GridViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {

        //populate views
        final Movie movie = mMovieList.get(position);

        holder.name.setText(movie.getName());
        try {
            Picasso.with(mContext)
                    .load(IMAGE_URL + movie.getImage())
                    .fit()
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(holder.image);
            Log.i("Picasso", "This is from Picasso1" + mMovieList );

        } catch (Exception e) {
            Log.i("Error", "This is from Picasso2", e);
        }

        //Set onClick Listener and gather all info to be displayed in DetailsActivity via implicit intent

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("Image", movie.getImage() );
                intent.putExtra("Overview", movie.getOverview());
                intent.putExtra("Name", movie.getName());
                intent.putExtra("movieId", movie.getMovieId());
                intent.putExtra("releaseDate", movie.getReleaseDate());
                intent.putExtra("rating", movie.getRating());
                intent.putExtra("backdrop", movie.getBackdrop());
                intent.putExtra("switch", 2);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
       return mMovieList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;


        public GridViewHolder(View itemView){
            super (itemView);

            image = itemView.findViewById(R.id.movie_img);
            name = itemView.findViewById(R.id.movie_name);

        }
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */

    public void setmMovieList(List<Movie> movies) {
        mMovieList = movies;
        notifyDataSetChanged();
    }


}
