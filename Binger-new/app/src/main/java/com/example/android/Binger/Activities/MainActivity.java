package com.example.android.Binger.Activities;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.Binger.Adapters.DataObjectAdapter;
import com.example.android.Binger.Adapters.ImageGridAdapter;
import com.example.android.Binger.Adapters.MovieJsonUtils;
import com.example.android.Binger.BuildConfig;
import com.example.android.Binger.Database.AppDatabase;
import com.example.android.Binger.Database.MainViewModel;
import com.example.android.Binger.Objects.DataObject;
import com.example.android.Binger.Objects.Movie;
import com.example.android.Binger.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static android.content.ContentValues.TAG;
import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>>{

    /*
     * This number will uniquely identify our Loader and is chosen arbitrarily. You can change this
     * to any number you like, as long as you use the same variable name.
     */
    private static final int MOVIEDB_SEARCH_LOADER = 22;
    private RecyclerView mImageGrid;
    public static List<Movie> movies;
    public static List<DataObject> favouriteMovies;
    private static final String THE_MOVIEDB_URL = "https://api.themoviedb.org/3/tv/";
    private static final String MOVIE_QUERY = "api_key";
    private static final String API_KEY = "";
    private String SEARCH_QUERY = "popular";
    ProgressBar mLoadingIndicator;
    AppDatabase mDb;
    ImageGridAdapter mAdapter;
    GridLayoutManager layoutManager;
    private String STATE_KEY = "recycler_state";
    private final String FAVOURITE_STATE_KEY = "favourite_recycler_state";
    private final String TOP_RATED = "top_rated_recycler_state";
    private final String POPULAR = "popular_recycler_state";
    private int FAVOURITE_INTENT_SWITCH;
    private int abc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiating database
        mDb = AppDatabase.getInstance(getApplicationContext());

        //getting the intent to show movie by Favourite
        Intent intent = getIntent();
        FAVOURITE_INTENT_SWITCH = 1;

        // abc works to cancel or make the progressbar invisible
        abc = 45;

        movies = new ArrayList<>();
        favouriteMovies = new ArrayList<>();

        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        mImageGrid = findViewById(R.id.rv_movies);

        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mImageGrid.setLayoutManager(layoutManager);

        mImageGrid.setHasFixedSize(true);

        mAdapter = new ImageGridAdapter(this, movies);

        mImageGrid.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(this, VERTICAL);
        mImageGrid.addItemDecoration(decoration);

        DividerItemDecoration decoration2 = new DividerItemDecoration(this, HORIZONTAL);
        mImageGrid.addItemDecoration(decoration2);

        if ( savedInstanceState == null ){
            if (intent.hasExtra("Favourite")){
                int favouriteMovie = intent.getIntExtra("Favourite", -2);
                if (favouriteMovie == FAVOURITE_INTENT_SWITCH) {
                    STATE_KEY = FAVOURITE_STATE_KEY;
                    loadFavouriteMovie();
                }
            } else {
                STATE_KEY = POPULAR;
                loadMoviesTask();
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if ((savedInstanceState != null) && (savedInstanceState.getString("key") != null)){
            if (savedInstanceState.getString("key") == FAVOURITE_STATE_KEY){
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                STATE_KEY = FAVOURITE_STATE_KEY;
                loadFavouriteMovie();
            }

            if (savedInstanceState.getString("key") == TOP_RATED){
                STATE_KEY = TOP_RATED;
                SEARCH_QUERY = "airing_today";
                loadMoviesTask();
            }

            if (savedInstanceState.getString("key") == POPULAR) {
                STATE_KEY = POPULAR;
                SEARCH_QUERY = "popular";
                loadMoviesTask();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("key", STATE_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( abc != 45){
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
        abc = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }



    //This method loads the the movie by calling LoaderManager
    private void loadMoviesTask(){
        Bundle queryBundle = new Bundle();
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Movie>> githubSearchLoader = loaderManager.getLoader(MOVIEDB_SEARCH_LOADER);
        // COMPLETED (23) If the Loader was null, initialize it. Else, restart it.
        if (githubSearchLoader == null) {
            loaderManager.initLoader(MOVIEDB_SEARCH_LOADER, queryBundle, this);

        } else {
            loaderManager.restartLoader(MOVIEDB_SEARCH_LOADER, queryBundle, this);
        }
    }

    //A method to build the URL to be queried
    private URL buildUrl(String stringUrl) {
        Uri uri = Uri.parse(THE_MOVIEDB_URL).buildUpon()
                .appendPath(stringUrl)
                .appendQueryParameter(MOVIE_QUERY, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException exception) {
            Log.e(TAG, "Error with creating URL", exception);
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else return null;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * AsyncTask rapped in LoaderManager
     * @param id
     * @param args this will take in Bundle and
     * @return List of Movies
     */

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Movie>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mLoadingIndicator.setVisibility(View.VISIBLE);
                //force a load.
                forceLoad();
            }


            @Override
            public List<Movie> loadInBackground() {
                // Create URL object
                URL url = buildUrl(SEARCH_QUERY);
                // Perform HTTP request to the URL and receive a JSON response back


                String jsonResponse = "";
                try {
                    jsonResponse = getResponseFromHttpUrl(url);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                // Extract relevant fields from the JSON response and create an {@link Movie} object
                movies = MovieJsonUtils.parseMovieJson(jsonResponse);

                // Return Movie object as the result
               return movies;
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {


        if (movies != null) {
            ImageGridAdapter rcv = new ImageGridAdapter(MainActivity.this, movies);
            mImageGrid.setAdapter(rcv);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mLoadingIndicator.clearFocus();

        } else {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

    }

    //This are for the menu item; it contains menu for choosing
    //movie either by popularity, top rated or favourite.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.param_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_popularity:
                SEARCH_QUERY = "popular";
                Log.i("url", "url: " + buildUrl(SEARCH_QUERY));
                STATE_KEY = POPULAR;
                loadMoviesTask();

                break;
            case R.id.menu_top_rated:
                SEARCH_QUERY = "top_rated";
                Log.i("url", "url: " + buildUrl(SEARCH_QUERY));
                STATE_KEY = TOP_RATED;
                loadMoviesTask();
                break;
            case R.id.menu_favourite:

                if (favouriteMovies == null) {
                    Toast.makeText(this, "You have no movie saved", Toast.LENGTH_LONG).show();
                }
                STATE_KEY = FAVOURITE_STATE_KEY;
                loadFavouriteMovie();
                
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //This method loads favourite movies for Room database and attach to the recycler view adapter
    private void loadFavouriteMovie() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getDataObj().observe(this, new Observer<List<DataObject>>() {
            @Override
            public void onChanged(@Nullable List<DataObject> dataObjects) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mAdapter.onDetachedFromRecyclerView(mImageGrid);
                DataObjectAdapter newAdapter = new DataObjectAdapter(MainActivity.this, dataObjects);
                mImageGrid.setAdapter(newAdapter);
                newAdapter.setmMovieList(dataObjects);
                STATE_KEY = FAVOURITE_STATE_KEY;
            }
        });
    }
}
