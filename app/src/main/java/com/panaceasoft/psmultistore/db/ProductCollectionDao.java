package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.Product;
import com.panaceasoft.psmultistore.viewobject.ProductCollection;
import com.panaceasoft.psmultistore.viewobject.ProductCollectionHeader;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Created by Panacea-Soft on 10/27/18.
 * Contact Email : teamps.is.cool@gmail.com
 */


@Dao
public abstract class ProductCollectionDao {

    //region Collection Header

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllCollectionHeader(List<ProductCollectionHeader> productCollectionHeaderList);

    @Query("SELECT * FROM ProductCollectionHeader ORDER BY addedDate DESC")
    public abstract LiveData<List<ProductCollectionHeader>> getAll();

    @Query("SELECT * FROM (SELECT * FROM ProductCollectionHeader ORDER BY addedDate DESC) WHERE shopId =:shopId LIMIT :limit")
    public abstract List<ProductCollectionHeader> getAllListByLimit(int limit, String shopId);

    @Query("SELECT * FROM Product WHERE id in " +
            "(SELECT productId FROM ProductCollection WHERE collectionId = :collectionId ) " +
            "ORDER BY addedDate DESC")
    public abstract LiveData<List<Product>> getProductListByCollectionId(String collectionId);

    @Query("SELECT * FROM " +
            "(SELECT * FROM Product WHERE id in " +
            "(SELECT productId FROM ProductCollection WHERE collectionId = :collectionId ) " +
            "ORDER BY addedDate DESC ) " +
            "LIMIT :limit")
    public abstract List<Product> getProductListByCollectionIdWithLimit(String collectionId, int limit);

    @Query("DELETE FROM ProductCollectionHeader")
    public abstract void deleteAll();

    public List<ProductCollectionHeader> getAllIncludingProductList(int collectionLimit, int productLimit,String shopId) {

        List<ProductCollectionHeader> productCollectionHeaderList = getAllListByLimit(collectionLimit,shopId);

        for (int i = 0; i < productCollectionHeaderList.size(); i++) {
            productCollectionHeaderList.get(i).productList = getProductListByCollectionIdWithLimit(productCollectionHeaderList.get(i).id, productLimit);
        }

        return productCollectionHeaderList;

    }
    //endregion

    //region Collection

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ProductCollection productCollection);

    @Query("DELETE FROM ProductCollection WHERE collectionId = :id ")
    public abstract void deleteAllBasedOnCollectionId(String id);

    //endregion

}
