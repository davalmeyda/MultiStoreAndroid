package com.panaceasoft.psmultistore.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.panaceasoft.psmultistore.viewobject.Country;

import java.util.List;

@Dao
public abstract class CountryDao {
    //region history

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Country country);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<Country> countryList);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(Country country);

    @Query("DELETE FROM Country")
    public abstract void deleteCountry();

    @Query("SELECT * FROM Country ORDER BY addedDate DESC")
    public abstract LiveData<List<Country>> getAllCountry();

    @Query("SELECT * FROM Country WHERE id=:countryId")
    public abstract LiveData<List<Country>> getCountryList(String countryId);

    @Query("DELETE FROM Country WHERE id =:id")
    public abstract void deleteCountryById(String id);

    //endregion
}
