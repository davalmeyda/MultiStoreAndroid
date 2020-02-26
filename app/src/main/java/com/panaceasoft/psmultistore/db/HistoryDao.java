package com.panaceasoft.psmultistore.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.panaceasoft.psmultistore.viewobject.HistoryProduct;

import java.util.List;

@Dao
public abstract class HistoryDao {
    //region history

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(HistoryProduct basket);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void update(HistoryProduct basket);

    @Query("DELETE FROM HistoryProduct")
    public abstract void deleteHistoryProduct();

    @Query("SELECT * FROM (SELECT * FROM HistoryProduct ORDER BY historyDate DESC) LIMIT :limit Offset 0")
    public abstract LiveData<List<HistoryProduct>> getAllHistoryProductListData(String limit);

    @Query("DELETE FROM HistoryProduct WHERE id =:id")
    public abstract void deleteHistoryProductById(String id);

    //endregion
}
