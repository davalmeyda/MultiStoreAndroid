package com.panaceasoft.psmultistore.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = "id")
public class ProductMap {

    @NonNull
    public final String id;

    public final String mapKey;

    public final String productId;

    public final int sorting;

    public final String addedDate;

    public ProductMap(@NonNull String id, String mapKey, String productId, int sorting, String addedDate) {
        this.id = id;
        this.mapKey = mapKey;
        this.productId = productId;
        this.sorting = sorting;
        this.addedDate = addedDate;
    }
}
