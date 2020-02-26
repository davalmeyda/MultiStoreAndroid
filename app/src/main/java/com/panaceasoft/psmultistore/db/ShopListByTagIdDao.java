package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.ShopByTagId;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ShopListByTagIdDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ShopByTagId shopByTagId);

    @Query("SELECT max(sorting) from ShopByTagId WHERE tagId = :value ")
    int getMaxSortingByValue(String value);

    @Query("DELETE FROM ShopByTagId WHERE tagId = :tagId")
    void deleteAllByTagId(String tagId);

    @Query("DELETE FROM ShopByTagId")
    void deleteAll();

}
