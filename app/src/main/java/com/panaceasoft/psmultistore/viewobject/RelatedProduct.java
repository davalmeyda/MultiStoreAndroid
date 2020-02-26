package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class RelatedProduct {
    @NonNull
    @PrimaryKey
    @SerializedName("id")
    public final String id;

    public final String shopId;

    public RelatedProduct(@NonNull String id, String shopId) {
        this.id = id;
        this.shopId = shopId;
    }
}
