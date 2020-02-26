package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.SubCategory;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface SubCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SubCategory subCategory);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(SubCategory subCategory);

    @Query("DELETE FROM SUBCATEGORY")
    void deleteAllSubCategory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SubCategory> subCategories);

    @Query("SELECT * FROM SUBCATEGORY ORDER BY addedDate DESC")
    LiveData<List<SubCategory>> getAllSubCategory();

    @Query("SELECT * FROM SUBCATEGORY WHERE catId=:catId")
    LiveData<List<SubCategory>> getSubCategoryList(String catId);


}
