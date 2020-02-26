package com.panaceasoft.psmultistore.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.panaceasoft.psmultistore.viewobject.Basket;
import com.panaceasoft.psmultistore.viewobject.Product;

import java.util.List;

@Dao
public abstract class BasketDao {
    //region basket

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Basket basket);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(Basket basket);

    @Query("SELECT * FROM Basket where shopId = :shopId")
    public abstract LiveData<List<Basket>> getAllBasketList(String shopId);

    @Query("SELECT id FROM Basket where productId = :productId and selectedAttributes = :selectedAttributes and selectedColorId = :selectedColorId")
    public abstract int getBasketId(String productId, String selectedAttributes, String selectedColorId);

    @Query("SELECT * FROM Basket WHERE shopId = :shopId")
    public abstract List<Basket> getAllBasketListData(String shopId);

    @Query("SELECT * FROM Product WHERE id = :productId LIMIT 1")
    public abstract Product getProductById(String productId);

    @Query("UPDATE Basket SET count =:count WHERE id = :id")
    public abstract void updateBasketById(int id,int count);

    @Query("DELETE FROM Basket WHERE id =:id")
    public abstract void deleteBasketById(int id);

    @Query("DELETE FROM Basket")
    public abstract void deleteAllBasket();

    public List<Basket> getAllBasketWithProduct(String shopId) {

        List<Basket> allBasketListData = getAllBasketListData(shopId);

        for (int i = 0; i < allBasketListData.size(); i++) {
            allBasketListData.get(i).product = getProductById(allBasketListData.get(i).productId);
        }

        return allBasketListData;

    }

    //endregion
}
