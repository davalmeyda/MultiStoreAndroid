package com.panaceasoft.psmultistore.db;


import com.panaceasoft.psmultistore.viewobject.Shop;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ShopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Shop> shops);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Shop shop);

    @Query("DELETE FROM SHOP")
    void deleteAll();

    @Query("SELECT * FROM SHOP WHERE id = :id")
    LiveData<Shop> getShopById(String id);

    @Query("DELETE FROM SHOP WHERE id = :id")
    void deleteShopById(String id);

    @Query("SELECT s.* FROM Shop s, ShopMap sm WHERE s.id = sm.productId AND sm.mapKey = :mapKey ORDER BY sm.sorting asc")
    LiveData<List<Shop>> getShopListByMapKey (String mapKey);

    @Query("SELECT s.* FROM Shop s, ShopByTagId sm WHERE s.id = sm.id AND sm.tagId = :tagId ORDER BY sm.sorting asc")
    LiveData<List<Shop>> getShopListByTagId (String tagId);

}
