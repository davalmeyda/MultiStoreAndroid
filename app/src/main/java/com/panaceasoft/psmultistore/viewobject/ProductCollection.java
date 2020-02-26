package com.panaceasoft.psmultistore.viewobject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Created by Panacea-Soft on 10/27/18.
 * Contact Email : teamps.is.cool@gmail.com
 */


@Entity
public class ProductCollection {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public final int id = 0;

    @NonNull
    public final String collectionId;

    public final String productId;

    public ProductCollection(@NonNull String collectionId, String productId) {
        this.collectionId = collectionId;
        this.productId = productId;
    }
}
