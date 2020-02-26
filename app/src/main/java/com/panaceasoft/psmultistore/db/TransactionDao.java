package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.TransactionObject;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TransactionObject transactionObject);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(TransactionObject transactionObject);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllTransactionList(List<TransactionObject> transactionObjectList);

    @Query("DELETE FROM TransactionObject")
    void deleteAllTransactionList();

    @Query("SELECT * FROM TransactionObject order by addedDate desc")
    LiveData<List<TransactionObject>> getAllTransactionList();

    @Query("DELETE FROM TransactionObject WHERE id = :id")
    void deleteTransactionById(String id);

    @Query("SELECT * FROM TransactionObject WHERE id = :id")
    LiveData<TransactionObject> getTransactionById(String id);
}
