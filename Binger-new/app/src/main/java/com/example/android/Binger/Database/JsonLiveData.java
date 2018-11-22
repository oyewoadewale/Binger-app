package com.example.android.Binger.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.Binger.Adapters.MovieJsonUtils;
import com.example.android.Binger.Objects.ArrayListObjects;
import com.example.android.Binger.Objects.DataObject;
import com.example.android.Binger.Objects.Review;
import com.example.android.Binger.Objects.Youtube;

import java.net.URL;
import java.util.ArrayList;

import static com.example.android.Binger.Activities.DetailActivity.buildUrl2;
import static com.example.android.Binger.Activities.DetailActivity.buildUrl3;
import static com.example.android.Binger.Activities.DetailActivity.movieId;
import static com.example.android.Binger.Activities.DetailActivity.reviews;
import static com.example.android.Binger.Activities.DetailActivity.youtube;
import static com.example.android.Binger.Activities.MainActivity.getResponseFromHttpUrl;

/**
 * Created by Adewale on 22/11/2018.
 */

public class JsonLiveData extends android.arch.lifecycle.LiveData<ArrayList<Youtube>> {

        private final Context context;

        public JsonLiveData(Context context){
            this.context= context;
            loadData();
        }


    private void loadData(){
        new AsyncTask<Void, Void, ArrayList<Youtube>>() {
            @Override
            protected ArrayList<Youtube> doInBackground(Void... voids) {
                String g = String.valueOf(movieId);

                // Create URL object
                URL url = buildUrl2(g);

                // Perform HTTP request to the URL and receive a JSON response back
                String jsonResponse = "";
                try {
                    jsonResponse = getResponseFromHttpUrl(url);
                } catch (Exception e) {

                    e.printStackTrace();
                }

                youtube = MovieJsonUtils.parseToYouTube(jsonResponse);
                return youtube;
            }

            @Override
            protected void onPostExecute(ArrayList<Youtube> strings) {

                setValue(youtube);
            }
        }.execute();
    }
}
