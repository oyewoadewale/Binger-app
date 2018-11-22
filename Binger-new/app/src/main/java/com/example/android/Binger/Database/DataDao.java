package com.example.android.Binger.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.example.android.Binger.Objects.DataObject;

import java.util.List;

/**
 * Created by Adewale on 22/11/2018.
 */

@Dao
public interface DataDao {

    @Query("SELECT * FROM Data ORDER BY id")
    LiveData<List<DataObject>> loadAllData();

    @Insert
    void insertData(DataObject dataObject);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateData(DataObject dataObject);

    @Delete
    void deleteData(DataObject dataObject);

    @Query("SELECT * FROM Data WHERE id= :id")
    DataObject loadById(int id);

    @Query("SELECT * FROM Data WHERE name= :name")
    DataObject loadByName(String name);
}
