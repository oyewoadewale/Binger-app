package com.example.android.Binger.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.Binger.Objects.Youtube;

import java.util.ArrayList;

/**
 * Created by Adewale on 22/11/2018.
 */

public class JsonViewModel extends AndroidViewModel {

    private final LiveData dat;

    public JsonViewModel(@NonNull Application application) {
        super(application);
        dat = new JsonLiveData(application);
    }

    public LiveData<ArrayList<Youtube>> getData(){
        return dat;
    }
}