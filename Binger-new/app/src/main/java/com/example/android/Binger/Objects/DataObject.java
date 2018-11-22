package com.example.android.Binger.Objects;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Adewale on 22/11/2018.
 */


@Entity(tableName = "Data")
public class DataObject implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String image;
    private String name;
    private String overview;
    private String rating;
    private String backdrop;
    private String releaseDate;
    private ArrayList<Youtube> trailers;
    private ArrayList<Review> movieReviews;

    @Ignore
    public DataObject() {

        this.image = image;
        this.name = name;
        this.overview = overview;
        this.rating = rating;
        this.backdrop = backdrop;
        this.releaseDate = releaseDate;
        this.trailers = trailers;
        this.movieReviews = movieReviews;
    }

    public DataObject(int id, String image, String name, String overview,
                      String rating, String backdrop, String releaseDate,
                      ArrayList<Youtube> trailers, ArrayList<Review> movieReviews) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.overview = overview;
        this.rating = rating;
        this.backdrop = backdrop;
        this.releaseDate = releaseDate;
        this.trailers = trailers;
        this.movieReviews = movieReviews;
    }

    @Ignore
    protected DataObject(Parcel in) {
        id = in.readInt();
        image = in.readString();
        name = in.readString();
        overview = in.readString();
        rating = in.readString();
        backdrop = in.readString();
        releaseDate = in.readString();
        trailers = in.createTypedArrayList(Youtube.CREATOR);
        movieReviews = in.createTypedArrayList(Review.CREATOR);
    }

    public static final Creator<DataObject> CREATOR = new Creator<DataObject>() {
        @Override
        public DataObject createFromParcel(Parcel in) {
            DataObject mDataObject = new DataObject();
            mDataObject.id = in.readInt();
            mDataObject.image = in.readString();
            mDataObject.name = in.readString();
            mDataObject.overview = in.readString();
            mDataObject.rating = in.readString();
            mDataObject.backdrop = in.readString();
            mDataObject.releaseDate = in.readString();
            mDataObject.trailers = in.createTypedArrayList(Youtube.CREATOR);
            mDataObject.movieReviews = in.createTypedArrayList(Review.CREATOR);

            return mDataObject;
        }

        @Override
        public DataObject[] newArray(int size) {
            return new DataObject[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList<Youtube> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<Youtube> trailers) {
        this.trailers = trailers;
    }

    public ArrayList<Review> getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(ArrayList<Review> movieReviews) {
        this.movieReviews = movieReviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeString(rating);
        dest.writeString(backdrop);
        dest.writeString(releaseDate);
        dest.writeTypedList(trailers);
        dest.writeTypedList(movieReviews);
    }
}