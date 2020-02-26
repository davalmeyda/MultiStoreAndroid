package com.panaceasoft.psmultistore.db;

import com.panaceasoft.psmultistore.viewobject.Comment;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class CommentDao {
    //region Discounts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Comment comment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAllCommentList(List<Comment> commentList);

    @Query("DELETE FROM Comment WHERE productId = :product_id")
    public abstract void deleteAllCommentList(String product_id);

    @Query("SELECT * FROM Comment WHERE productId = :product_id order by addedDate desc")
    public abstract LiveData<List<Comment>> getAllCommentList(String product_id);

    @Query("DELETE FROM Comment")
    public abstract void deleteAll();

}
