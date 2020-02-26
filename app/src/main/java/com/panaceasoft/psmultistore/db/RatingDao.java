package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.Rating;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface RatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Rating> ratingList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Rating rating);

    @Query("DELETE FROM Rating")
    void deleteAll();

    @Query("SELECT * FROM Rating WHERE productId = :productId")
    LiveData<List<Rating>> getRatingById(String productId);

}
