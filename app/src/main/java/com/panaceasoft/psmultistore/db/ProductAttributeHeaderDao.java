package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.ProductAttributeDetail;
import com.panaceasoft.psmultistore.viewobject.ProductAttributeHeader;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class ProductAttributeHeaderDao {
    //region product attribute header

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ProductAttributeHeader> productAttributeHeaderList);

    @Query("SELECT * FROM ProductAttributeHeader WHERE productId =:productId order by addedDate desc")
    public abstract List<ProductAttributeHeader> getProductAttributeHeaderById(String productId);

    @Query("SELECT * FROM ProductAttributeDetail WHERE productId =:productId AND headerId=:headerId")
    public abstract List<ProductAttributeDetail> getProductAttributeDetailById(String productId, String headerId);

    @Query("DELETE FROM ProductAttributeHeader WHERE productId =:productId")
    public abstract void deleteProductAttributeHeaderById(String productId);

    @Query("DELETE FROM ProductAttributeHeader")
    public abstract void deleteAll();

    public List<ProductAttributeHeader> getProductAttributeHeaderAndDetailById(String productId) {

        List<ProductAttributeHeader> productAttributeHeaderList = getProductAttributeHeaderById(productId);

        for (int i = 0; i < productAttributeHeaderList.size(); i++) {
            productAttributeHeaderList.get(i).attributesDetailList = getProductAttributeDetailById(productId, productAttributeHeaderList.get(i).id);
        }

        return productAttributeHeaderList;

    }
}
