package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.ShopTag;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ShopTagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<ShopTag> shopCategories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ShopTag shopTag);

    @Query("DELETE FROM ShopTag")
    void deleteALl();

    @Query("SELECT * FROM ShopTag ORDER By addedDate desc")
    LiveData<List<ShopTag>> getAllShopCategories();

}
