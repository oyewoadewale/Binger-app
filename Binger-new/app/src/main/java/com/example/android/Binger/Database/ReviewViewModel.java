package com.example.android.Binger.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.Binger.Objects.Review;
import com.example.android.Binger.Objects.Youtube;

import java.util.ArrayList;

/**
 * Created by Adewale on 22/11/2018.
 */
public class ReviewViewModel extends AndroidViewModel {

    private final LiveData rev;

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        rev = new ReviewLiveData(application);
    }

    public LiveData<ArrayList<Review>> getData(){
        return rev;
    }

}
