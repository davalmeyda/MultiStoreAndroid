package com.panaceasoft.psmultistore.viewobject;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = "id")
public class CategoryMap {

    @NonNull
    public final String id;

    public final String mapKey;

    public final String categoryId;

    public final int sorting;

    public final String addedDate;

    public CategoryMap(@NonNull String id, String mapKey, String categoryId, int sorting, String addedDate) {
        this.id = id;
        this.mapKey = mapKey;
        this.categoryId = categoryId;
        this.sorting = sorting;
        this.addedDate = addedDate;
    }
}
