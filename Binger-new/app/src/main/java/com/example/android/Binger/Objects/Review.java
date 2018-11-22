package com.example.android.Binger.Objects;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Adewale on 22/11/2018.
 */

public class Review implements Parcelable {
    private String author;
    private String content;


    public Review () {
    }

    public Review (String author, String content) {
        this.author = author;
        this.content = content;
    }

    protected Review(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            Review mReview = new Review();
            mReview.author = in.readString();
            mReview.content = in.readString();
            return mReview;
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}