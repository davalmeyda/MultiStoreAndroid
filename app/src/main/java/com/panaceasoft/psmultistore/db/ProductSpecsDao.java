package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.ProductSpecs;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class ProductSpecsDao {
    //region product color

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ProductSpecs> productSpecsList);

    @Query("SELECT * FROM ProductSpecs WHERE productId =:productId")
    public abstract LiveData<List<ProductSpecs>> getProductSpecsById(String productId);

    @Query("DELETE FROM ProductSpecs WHERE productId =:productId")
    public abstract void deleteProductSpecsById(String productId);

    @Query("DELETE FROM ProductSpecs")
    public abstract void deleteAll();
}
