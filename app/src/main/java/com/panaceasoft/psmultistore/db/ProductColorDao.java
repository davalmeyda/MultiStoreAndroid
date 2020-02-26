package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.ProductColor;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class ProductColorDao {

    //region product color

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ProductColor> productColorList);

    @Query("SELECT * FROM ProductColor WHERE productId =:productId")
    public abstract LiveData<List<ProductColor>> getProductColorById(String productId);

    @Query("DELETE FROM ProductColor WHERE productId =:productId")
    public abstract void deleteProductColorById(String productId);

    @Query("DELETE FROM ProductColor")
    public abstract void deleteAll();

}
