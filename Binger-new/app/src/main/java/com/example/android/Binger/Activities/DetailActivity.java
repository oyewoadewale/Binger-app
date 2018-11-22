package com.example.android.Binger.Activities;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.util.TableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.Binger.Adapters.DataObjectAdapter;
import com.example.android.Binger.Adapters.FavouriteReviewsAdapter;
import com.example.android.Binger.Adapters.MovieJsonUtils;
import com.example.android.Binger.Adapters.ReviewAdapter;
import com.example.android.Binger.Adapters.TrailerViewAdapter;
import com.example.android.Binger.Database.AppDatabase;
import com.example.android.Binger.Database.AppExecutors;
import com.example.android.Binger.Database.JsonViewModel;
import com.example.android.Binger.Database.MainViewModel;
import com.example.android.Binger.Database.ReviewViewModel;
import com.example.android.Binger.Objects.ArrayListObjects;
import com.example.android.Binger.Objects.DataObject;
import com.example.android.Binger.Objects.Review;
import com.example.android.Binger.Objects.Youtube;
import com.example.android.Binger.R;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DetailActivity extends AppCompatActivity {

    TextView overview_tv;
    ImageView image_tv;
    TextView name_tv;
    TextView ratings;
    TextView trailerHeader;
    TextView reviewHeader;
    TextView moreReviews;
    TextView moreTrailers;
    TextView shareTrailer;
    Context context;
    TextView release_date;
    ImageView backdrop_poster;
    private ExpandableHeightListView trailers;
    private ExpandableHeightListView reviewPage;
    public static ArrayList<Youtube> youtube;
    ArrayList<Youtube> firstTrailer;
    ArrayList<Youtube> otherTrailers;

    public static ArrayList<Review> reviews;
    ArrayList<Review> firstReview;
    ArrayList<Review> otherReviews;
    TrailerViewAdapter adapter;
    ReviewAdapter reviewsAdapter;
    public static DataObject data;
    DataObject dataObject;
    DataObject findNameFromDatabase;
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private static final String THE_MOVIEDB_URL2 = "https://api.themoviedb.org/3/movie/";
    private static final String MOVIE_QUERY2 = "api_key";
    private static final String API_KEY2 = "";
    private static String SEARCH_QUERY2 = "videos";
    public static int movieId;
    Button favourite;
    String movieRating;
    File file;
    String name;
    String overview;
    String releaseDate;
    String favouriteName;
    String firstAuthor;
    String firstContent;
    String firstKey;

    int dataId;
    int switcher;
    int adapterPosition;

    AppDatabase mDb;


    //Navigation arrow on the action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //instantiating the database.
        mDb = AppDatabase.getInstance(getApplicationContext());

        youtube = new ArrayList<Youtube>();
        firstTrailer = new ArrayList<Youtube>();
        otherTrailers = new ArrayList<Youtube>();
        reviews = new ArrayList<Review>();
        firstReview = new ArrayList<Review>();
        otherReviews = new ArrayList<Review>();


        adapter = new TrailerViewAdapter(this, youtube);
        reviewsAdapter = new ReviewAdapter(this, reviews);


        //Credit to Paolorotolo @github for making ListViews go with ScrollView
        trailers = findViewById(R.id.expandable_list);
        trailers.setAdapter(adapter);
        trailers.setExpanded(true);

        reviewPage = findViewById(R.id.expandable_list_of_reviews);
        reviewPage.setAdapter(reviewsAdapter);
        reviewPage.setExpanded(true);



        //Navigation arrow on the acton bar; check also override onOptionsItemSelected
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        context = getApplicationContext();

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        switcher = getIntent().getIntExtra("switch", 3);
        //switcher = intent.getIntExtra("switch", 3);
        if (intent != null && intent.hasExtra("position")) {
            adapterPosition = intent.getIntExtra("position", -1);
        }


        overview_tv = findViewById(R.id.overview);
        image_tv = findViewById(R.id.image);
        name_tv = findViewById(R.id.name);
        ratings = findViewById(R.id.ratings);
        release_date = findViewById(R.id.release_date);
        backdrop_poster = findViewById(R.id.backdrop_poster);


        favourite = findViewById(R.id.favourite_button);
        trailerHeader = findViewById(R.id.trailer_header);
        reviewHeader = findViewById(R.id.review_heading);
        moreReviews = findViewById(R.id.more_reviews);
        moreTrailers = findViewById(R.id.more_trailers);
        shareTrailer = findViewById(R.id.share_trailer);
        addListenerOnRatingBar(ratings);


        //Switcher is the intent to be used to switch the UI to show favourite movie if selected
        // everything that goes inside the following if statemenet is for favourite moview.
        if (switcher != 2) {

            favourite.setText("Unfavourite");

            dataObject = (DataObject) getIntent().getParcelableExtra("array");
            dataId = dataObject.getId();
            favouriteName = dataObject.getName();
            final String favouriteOverview = dataObject.getOverview();
            final String favouriteReleaseDate = dataObject.getReleaseDate();
            final ArrayList<Youtube> savedTrailer = dataObject.getTrailers();
            ArrayList<Review> savedReview = dataObject.getMovieReviews();
            movieRating = dataObject.getRating();

            name_tv.setText(favouriteName);
            overview_tv.setText(favouriteOverview);
            ratings.setText("Rating: " + movieRating);
            release_date.setText("Release Date: " + favouriteReleaseDate);

            //These strings will be used to save and retrieve images from SD card or internal memory
            String imagePath = name_tv.getText().toString() + "0i";
            String backdropPath = name_tv.getText().toString() + "1b";

            //downloadImage() gets images form SD card
            try {
                DataObjectAdapter.downloadImage(imagePath, image_tv, this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                DataObjectAdapter.downloadImage(backdropPath, backdrop_poster, context);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ((savedTrailer != null) && (savedTrailer.size() != 0)) {

                shareTrailer.setVisibility(View.VISIBLE);
                shareTrailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String firstTrailerKey = savedTrailer.get(0).getKey();
                        Intent share = new Intent( Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_SUBJECT, "Youtube Trailer");
                        share.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + firstTrailerKey);
                        startActivity(Intent.createChooser(share, "Share Trailer"));
                    }
                });
                TrailerViewAdapter lv = new TrailerViewAdapter(DetailActivity.this, savedTrailer);
                trailers.setAdapter(lv);
                switcher = 3;
            }

            if (savedTrailer.size() == 0) {
                trailerHeader.setText("No Trailer to display");
                trailerHeader.setTextSize(20);
            }

            if (savedReview != null){
                ReviewAdapter rv = new ReviewAdapter(DetailActivity.this, savedReview);
                reviewPage.setAdapter(rv);
            }
            if (savedReview.size() == 0) {
                reviewHeader.setText("No Review to display");
                reviewHeader.setTextSize(20);
            }

        } else {
            name = getIntent().getStringExtra("Name");
            overview = getIntent().getStringExtra("Overview");
            final String image = getIntent().getStringExtra("Image");
            movieId = getIntent().getIntExtra("movieId", 1);
            final String backdrop = getIntent().getStringExtra("backdrop");
            releaseDate = getIntent().getStringExtra("releaseDate");
            movieRating = getIntent().getStringExtra("rating");
            Log.i("this", "switch " + switcher);

            name_tv.setText(name);
            overview_tv.setText(overview);
            ratings.setText("Rating: " + movieRating);
            release_date.setText("Release Date: " + releaseDate);

            //load backdrop poster
            Picasso.with(context)
                    .load(IMAGE_URL + backdrop)
                    .fit()
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(backdrop_poster);

            Picasso.with(context)
                    .load(IMAGE_URL + image)
                    .fit()
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .into(image_tv);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    findNameFromDatabase = mDb.dataDao().loadByName(name);
                    Log.i("this", "nameInDatabase: " + name);
                }
            });


            if (findNameFromDatabase != null){
                favourite.setText("Unfavourite");
            }

            // This loads the youtube key from the JSON got from themovieDB
            JsonViewModel model= ViewModelProviders.of(this).get(JsonViewModel.class);
            model.getData().observe(this, new Observer<ArrayList<Youtube>>() {
                @Override
                public void onChanged(@Nullable final ArrayList<Youtube> dat) {

                    if (dat != null && dat.size() >= 1) {
                        otherTrailers = dat;
                        firstKey = dat.get(0).getKey();
                        Youtube yt = new Youtube();
                        yt.setKey(firstKey);
                        firstTrailer.add(yt);
                        moreTrailers.setVisibility(View.VISIBLE);

                        TrailerViewAdapter lv = new TrailerViewAdapter(DetailActivity.this, firstTrailer);
                        trailers.setAdapter(lv);

                        shareTrailer.setVisibility(View.VISIBLE);
                        shareTrailer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent share = new Intent( Intent.ACTION_SEND);
                                share.setType("text/plain");
                                share.putExtra(Intent.EXTRA_SUBJECT, "Youtube Trailer");
                                share.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + firstKey);
                                startActivity(Intent.createChooser(share, "Share Trailer"));

                            }
                        });

                        moreTrailers.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {

                                                             //if (firstTrailer != null && otherTrailers != null){

                                                                   if (moreTrailers.getText() == "View less trailers") {
                                                                       TrailerViewAdapter lv = new TrailerViewAdapter
                                                                               (DetailActivity.this, firstTrailer);
                                                                       trailers.setAdapter(lv);
                                                                       moreTrailers.setText("View more trailers");

                                                                   } else {

                                                                       TrailerViewAdapter lv = new TrailerViewAdapter
                                                                               (DetailActivity.this, otherTrailers);
                                                                       trailers.setAdapter(lv);
                                                                       moreTrailers.setText("View less trailers");
                                                                   }
                                                             //}
                                                           }
                                                       }
                        );
                    }
                    else {
                        trailerHeader.setText("No Trailer to display");
                        trailerHeader.setTextSize(20);
                    }

                }
            });


            // This loads the reviews from the JSON got from themovieDB

            ReviewViewModel reviewModel= ViewModelProviders.of(this).get(ReviewViewModel.class);
            reviewModel.getData().observe( this, new Observer<ArrayList<Review>>() {
                @Override
                public void onChanged(@Nullable ArrayList<Review> rev) {

                    if (rev != null && rev.size() >= 1) {

                        otherReviews = rev;
                        firstAuthor = rev.get(0).getAuthor();
                        firstContent = rev.get(0).getContent();
                        Review rt = new Review();
                        rt.setAuthor(firstAuthor);
                        rt.setContent(firstContent);
                        firstReview.add(rt);
                        moreReviews.setVisibility(View.VISIBLE);

                        ReviewAdapter rv = new ReviewAdapter(DetailActivity.this, firstReview);
                        reviewPage.setAdapter(rv);



                        moreReviews.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {

                                                               //if (firstReview != null && otherReviews != null){

                                                                   if (moreReviews.getText() == "View less reviews") {
                                                                       ReviewAdapter rv = new ReviewAdapter
                                                                               (DetailActivity.this, firstReview);
                                                                       reviewPage.setAdapter(rv);
                                                                       moreReviews.setText("View more reviews");


                                                                   } else {

                                                                       ReviewAdapter rv = new ReviewAdapter
                                                                               (DetailActivity.this, otherReviews);
                                                                       reviewPage.setAdapter(rv);
                                                                       moreReviews.setText("View less reviews");
                                                                   }
                                                               //}
                                                           }
                                                       }
                        );
                    }
                    else {
                        reviewHeader.setText("No Review to display");
                        reviewHeader.setTextSize(20);
                    }
                }
            });
        }

        favourite.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             if (favourite.getText() == "Unfavourite"){

                                                 AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                                     @Override
                                                     public void run() {

                                                         DataObject d = mDb.dataDao().loadById(dataId);
                                                         Log.i("this", "viewholder " + dataId);

                                                         if (d != null) {
                                                         mDb.dataDao().deleteData(d);
                                                         finish();
                                                         }
                                                     }
                                                 });

                                                 Toast.makeText(context,
                                                         "The movie is deleted",
                                                         Toast.LENGTH_LONG).show();


                                             } else {
                                                 data = new DataObject();
                                                 data.setName(name);
                                                 data.setOverview(overview);
                                                 data.setRating(movieRating);
                                                 data.setReleaseDate(releaseDate);
                                                 data.setTrailers(youtube);
                                                 data.setMovieReviews(reviews);

                                                 try {
                                                     saveImage(name_tv.getText().toString() + "0i", image_tv);
                                                     saveImage(name_tv.getText().toString() + "1b", backdrop_poster);
                                                 } catch (IOException e) {
                                                     e.printStackTrace();
                                                 }

                                                 Toast.makeText(context,
                                                         "The movie is saved as a favourite",
                                                         Toast.LENGTH_LONG).show();

                                                 AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                                     @Override
                                                     public void run() {

                                                         findNameFromDatabase = mDb.dataDao().loadByName(name);
                                                         Log.i("this", "nameInDatabase: " + name);

                                                         if (findNameFromDatabase == null){
                                                         mDb.dataDao().insertData(data);
                                                         }
                                                     }
                                                 });

                                             }
                                         }
                                     }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * saveImage() gets and saves images to SD card or internal memory of the device
     * @param fileName
     * @param image
     * @throws FileNotFoundException
     */
    private void saveImage(String fileName, ImageView image) throws FileNotFoundException {

        Bitmap bitmapImage = ((BitmapDrawable) image.getDrawable()).getBitmap();
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path + "/PopularMovie/");
        dir.mkdirs();

        file = new File(dir + "/" + fileName + ".png/");
        Log.i("File path ", "File Path " + file);

        if (image == image_tv) {
            data.setImage(file.toString());
        }


        data.setBackdrop(file.toString());

        FileOutputStream out = null;

        try {
            out = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, "Error getting intents", Toast.LENGTH_SHORT).show();
    }


    //This method builds the URL for getting the youtube key
    public static URL buildUrl2(String stringUrl) {

        Uri uri = Uri.parse(THE_MOVIEDB_URL2).buildUpon()
                .appendPath(stringUrl)
                .appendPath(SEARCH_QUERY2)
                .appendQueryParameter(MOVIE_QUERY2, API_KEY2)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException exception) {
            Log.e(TAG, "Error creating URL", exception);
        }
        return url;
    }


    public void addListenerOnRatingBar(final TextView textView) {
        RatingBar ratingBar = findViewById(R.id.favourite_rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar1, float rating, boolean fromUser) {
                textView.setText("Rating: " + String.valueOf(rating * 2) + " /10");
            }
        });
    }


    //This method builds the URL for downloading review
    public static URL buildUrl3(String stringUrl) {

        Uri uri = Uri.parse(THE_MOVIEDB_URL2).buildUpon()
                .appendPath(stringUrl)
                .appendPath("reviews")
                .appendQueryParameter(MOVIE_QUERY2, API_KEY2)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException exception) {
            Log.e(TAG, "Error creating URL", exception);
        }
        return url;
    }
}