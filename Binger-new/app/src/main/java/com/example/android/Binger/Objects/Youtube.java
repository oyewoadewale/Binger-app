package com.example.android.Binger.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Adewale on 22/11/2018.
 */

public class Youtube implements Parcelable{
    private String key;

    public Youtube () {
    }

    public Youtube (String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //All that relates to parcelable

    protected Youtube(Parcel in) {
        key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);

    }

    public static final Creator<Youtube> CREATOR = new Creator<Youtube>() {
        @Override
        public Youtube createFromParcel(Parcel in) {
            Youtube mYoutube = new Youtube();
            mYoutube.key = in.readString();
            return mYoutube;
        }

        @Override
        public Youtube[] newArray(int size) {
            return new Youtube[size];
        }
    };


}
