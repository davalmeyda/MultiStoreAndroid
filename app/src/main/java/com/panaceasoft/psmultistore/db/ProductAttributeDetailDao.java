package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.ProductAttributeDetail;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class ProductAttributeDetailDao {
    //region product attribute detail

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ProductAttributeDetail> productAttributeDetailList);

    @Query("SELECT * FROM ProductAttributeDetail WHERE productId =:productId AND headerId=:headerId")
    public abstract LiveData<List<ProductAttributeDetail>> getProductAttributeDetailById(String productId,String headerId);

    @Query("DELETE FROM ProductAttributeDetail WHERE productId =:productId AND headerId=:headerId")
    public abstract void deleteProductAttributeDetailById(String productId,String headerId);

    @Query("DELETE FROM productattributedetail")
    public abstract void deleteAll();
}
