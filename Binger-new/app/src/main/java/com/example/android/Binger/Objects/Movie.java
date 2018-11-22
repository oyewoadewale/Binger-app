package com.example.android.Binger.Objects;


/**
 * Created by Adewale on 22/11/2018.
 */

public class Movie {
    private String image;
    private String name;
    private String overview;
    private int movieId;
    private String rating;
    private String backdrop;
    private String releaseDate;


    public Movie() {
    }

    public Movie(String image, String name, String overview, int  movieId, String rating, String backdrop, String releaseDate) {
        this.image = image;
        this.name = name;
        this.overview = overview;
        this.movieId = movieId;
        this.rating = rating;
        this.backdrop = backdrop;
        this.releaseDate = releaseDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating (String rating) {
        this.rating = rating;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop (String backdrop) {
        this.backdrop = backdrop;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate (String releaseDate) {
        this.releaseDate = releaseDate;
    }
}