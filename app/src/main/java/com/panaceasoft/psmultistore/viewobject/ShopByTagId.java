package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = "id")
public class ShopByTagId {

    @NonNull
    @SerializedName("id")
    public String id;

    public int sorting;

    public String tagId;

    public ShopByTagId(@NonNull String id, int sorting, String tagId) {
        this.id = id;
        this.sorting = sorting;
        this.tagId = tagId;
    }
}
