package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.ProductMap;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ProductMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductMap productMap);

    @Query("DELETE FROM ProductMap WHERE mapKey = :key")
    void deleteByMapKey(String key);

    @Query("SELECT max(sorting) from ProductMap WHERE mapKey = :value ")
    int getMaxSortingByValue(String value);

    @Query("SELECT * FROM ProductMap")
    List<ProductMap> getAll();

    @Query("DELETE FROM ProductMap")
    void deleteAll();
}
