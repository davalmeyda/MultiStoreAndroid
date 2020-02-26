package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProductListByCatId {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    public final String productId;

    @NonNull
    @SerializedName("cat_id")
    public final String catId;

    public ProductListByCatId(@NonNull String productId, @NonNull String catId) {
        this.productId = productId;
        this.catId = catId;
    }
}
