package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.ShopMap;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Created by Panacea-Soft on 3/19/19.
 * Contact Email : teamps.is.cool@gmail.com
 */


@Dao
public interface ShopMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ShopMap shopMap);

    @Query("DELETE FROM ShopMap WHERE mapKey = :key")
    void deleteByMapKey(String key);

    @Query("SELECT max(sorting) from ShopMap WHERE mapKey = :value ")
    int getMaxSortingByValue(String value);

    @Query("DELETE FROM ShopMap")
    void deleteAll();
}
