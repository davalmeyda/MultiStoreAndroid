package com.panaceasoft.psmultistore.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.panaceasoft.psmultistore.viewobject.City;

import java.util.List;

@Dao
public abstract class CityDao {
//region history

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(City city);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<City> cityList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(City city);

    @Query("DELETE FROM City")
    public abstract void deleteCity();

    @Query("SELECT * FROM City ORDER BY addedDate DESC")
    public abstract LiveData<List<City>> getAllCity();

    @Query("SELECT * FROM City WHERE id=:cityId")
    public abstract LiveData<List<City>> getCityList(String cityId);

    @Query("DELETE FROM City WHERE id =:id")
    public abstract void deleteCityById(String id);

    //endregion
}
