package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.ShippingMethod;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ShippingMethodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ShippingMethod> shippingMethods);

    @Query("SELECT * FROM ShippingMethod Where shopId = :shopId")
    LiveData<List<ShippingMethod>> getShippingMethodsByShopId(String shopId);

    @Query("DELETE FROM ShippingMethod")
    void deleteAll();
}
