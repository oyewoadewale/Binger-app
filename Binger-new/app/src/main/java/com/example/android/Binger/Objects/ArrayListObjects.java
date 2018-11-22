package com.example.android.Binger.Objects;

import java.util.ArrayList;

/**
 * Created by Adewale on 22/11/2018.
 */

public class ArrayListObjects {

    private ArrayList<Youtube> mTrailers;
    private ArrayList<Review> mMovieReviews;

    public ArrayListObjects(){

    }

    public ArrayListObjects( ArrayList<Youtube> trailers, ArrayList<Review> movieReviews){
        this.mTrailers = trailers;
        this.mMovieReviews = movieReviews;
    }

    public ArrayList<Youtube> getmTrailers() {
        return mTrailers;
    }

    public void setmTrailers(ArrayList<Youtube> mTrailers) {
        this.mTrailers = mTrailers;
    }

    public ArrayList<Review> getmMovieReviews() {
        return mMovieReviews;
    }

    public void setmMovieReviews(ArrayList<Review> mMovieReviews) {
        this.mMovieReviews = mMovieReviews;
    }
}
