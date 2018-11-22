package com.example.android.Binger.Database;

import android.arch.persistence.room.TypeConverter;

import com.example.android.Binger.Objects.Review;
import com.example.android.Binger.Objects.Youtube;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Adewale on 22/11/2018.
 */

public class ArrayListConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<Youtube> stringToObjectList(String data){
        if (data ==null) {
            return null;
        }

        Type listType = new TypeToken<ArrayList<Youtube>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String objectListToString(ArrayList<Youtube> arrayList){
        return gson.toJson(arrayList);
    }

    @TypeConverter
    public static ArrayList<Review> stringToList(String data){
        if (data ==null) {
            return null;
        }

        Type lisType = new TypeToken<ArrayList<Review>>() {}.getType();
        return gson.fromJson(data, lisType);
    }

    @TypeConverter
    public static String listToString(ArrayList<Review> arrayList){
        return gson.toJson(arrayList);
    }
}
