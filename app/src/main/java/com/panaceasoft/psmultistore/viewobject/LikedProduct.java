package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LikedProduct {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public final int id = 0;

    @NonNull
    @SerializedName("id")
    public final String productId;

    public LikedProduct(@NonNull String productId) {
        this.productId = productId;
    }
}