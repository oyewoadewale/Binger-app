package com.example.android.Binger.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.Binger.Activities.DetailActivity;
import com.example.android.Binger.Database.AppDatabase;
import com.example.android.Binger.Objects.DataObject;
import com.example.android.Binger.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;


/**
 * Created by Adewale on 22/11/2018.
 */

public class DataObjectAdapter extends RecyclerView.Adapter<DataObjectAdapter.FavouriteGridViewHolder> {
    String mName;
    String mImage;
    static Context mContext;
    Context context1;
    private List<DataObject> mMovieList = null;
    public static String imageFileName;
    public static String backdropFileName;
    AppDatabase mDb;

    private  static final String IMAGE_URL = "http://image.tmdb.org/t/p/w300/";


    public DataObjectAdapter(Context context, List<DataObject> movieList){
        mContext = context;
        mMovieList = movieList;
    }

    @Override
    public FavouriteGridViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.image_grid;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        FavouriteGridViewHolder viewHolder = new FavouriteGridViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder( final FavouriteGridViewHolder holder, int position) {

        //populate views
        final DataObject movie = mMovieList.get(position);


        holder.name.setText(movie.getName());
        imageFileName = holder.name.getText().toString() + "0i";
        backdropFileName = holder.name.getText().toString() + "1b";
        try {
            downloadImage(imageFileName, holder.image, mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Set onClick Listener and gather all info to be displayed in DetailsActivity via implicit intent
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("array",movie);
                intent.putExtras(bundle);
                intent.putExtra("position", holder.getAdapterPosition());
                intent.putExtra("switch", 3);
                mContext.startActivity(intent);
            }
        });



    }






    public static void downloadImage(String fileName, ImageView image, Context context){

        File downloadFolder = Environment.getExternalStorageDirectory();
        File dir = new File(downloadFolder+ "/PopularMovie/");
        Uri downloadPath = Uri.fromFile(new File(dir, "/" + fileName + ".png/"));

        if (downloadPath.toString() != null){
            Picasso.with(context).load(downloadPath).placeholder(R.drawable.placeholder_image).into(image);

        } else {
            Toast.makeText(context, "There is no image of this name saved",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public static class FavouriteGridViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;


        public FavouriteGridViewHolder(View itemView){
            super (itemView);

            image = itemView.findViewById(R.id.movie_img);
            name = itemView.findViewById(R.id.movie_name);

        }
    }

    public List<DataObject> getMovieList() {
        return mMovieList;
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */

    public void setmMovieList(List<DataObject> movies) {
        mMovieList = movies;
        notifyDataSetChanged();
    }
}

