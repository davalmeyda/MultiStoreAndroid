package com.panaceasoft.psmultistore.viewobject;


import androidx.room.Entity;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id")
public class FeaturedProduct {

    @SerializedName("id")
    @NonNull
    public final String id;

    public FeaturedProduct(@NonNull String id) {
        this.id = id;
    }
}
