package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

/**
 * Created by Panacea-Soft on 10/27/18.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Entity(primaryKeys = "id")
public class ProductCollectionHeader {

    @SerializedName("id")
    @NonNull
    public final String id;

    @SerializedName("shop_id")
    @NonNull
    public final String shopId;

    @SerializedName("name")
    public final String name;

    @SerializedName("status")
    public final String status;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("added_user_id")
    public final String addedUserId;

    @SerializedName("updated_date")
    public final String updatedDate;

    @SerializedName("updated_user_id")
    public final String updatedUserId;

    @SerializedName("updated_flag")
    public final String updatedFlag;

    @SerializedName("added_date_str")
    public final String addedDateStr;

    @Embedded(prefix = "default_photo_")
    @SerializedName("default_photo")
    public final Image defaultPhoto;

    @Ignore
    @SerializedName("products")
    public List<Product> productList;

    public ProductCollectionHeader(@NonNull String id, @NonNull String shopId, String name, String status, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String updatedFlag, String addedDateStr, Image defaultPhoto) {
        this.id = id;
        this.shopId = shopId;
        this.name = name;
        this.status = status;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.addedDateStr = addedDateStr;
        this.defaultPhoto = defaultPhoto;
    }
}
