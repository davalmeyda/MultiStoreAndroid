package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.Category;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> categories);

    @Query("DELETE FROM Category")
    void deleteAllCategory();

    @Query("DELETE FROM Category WHERE id = :id")
    void deleteCategoryById(String id);

    @Query("SELECT c.* FROM Category c, CategoryMap cm WHERE c.id = cm.categoryId AND cm.mapKey = :value ORDER BY cm.sorting asc")
    LiveData<List<Category>> getCategoryByKeyTest (String value);

}