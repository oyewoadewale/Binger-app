package com.example.android.Binger.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.Binger.Adapters.MovieJsonUtils;
import com.example.android.Binger.Objects.Review;
import com.example.android.Binger.Objects.Youtube;

import java.net.URL;
import java.util.ArrayList;

import static com.example.android.Binger.Activities.DetailActivity.buildUrl3;
import static com.example.android.Binger.Activities.DetailActivity.movieId;
import static com.example.android.Binger.Activities.DetailActivity.reviews;
import static com.example.android.Binger.Activities.MainActivity.getResponseFromHttpUrl;

/**
 * Created by Adewale on 22/11/2018.
 */

public class ReviewLiveData extends android.arch.lifecycle.LiveData<ArrayList<Review>> {

        private final Context context;

        public ReviewLiveData(Context context){
            this.context= context;
            loadReview();
        }


    private void loadReview(){
        new AsyncTask<Void, Void, ArrayList<Review>>() {
            @Override
            protected ArrayList<Review> doInBackground(Void... voids) {
                String g = String.valueOf(movieId);

                // Create URL object
                URL url = buildUrl3(g);

                // Perform HTTP request to the URL and receive a JSON response back
                String jsonResponse = "";
                try {
                    jsonResponse = getResponseFromHttpUrl(url);
                } catch (Exception e) {

                    e.printStackTrace();
                }

                reviews = MovieJsonUtils.parseReview(jsonResponse);
                return reviews;
            }

            @Override
            protected void onPostExecute(ArrayList<Review> strings) {

                setValue(reviews);
            }
        }.execute();
    }
}
