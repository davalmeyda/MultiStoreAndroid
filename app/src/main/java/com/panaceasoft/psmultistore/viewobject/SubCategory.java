package com.panaceasoft.psmultistore.viewobject;


import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(primaryKeys = "id")
public class SubCategory {

    @SerializedName("id")
    @NonNull
    public final String id;

    @SerializedName("cat_id")
    public final String catId;

    @SerializedName("shop_id")
    public final String shopId;

    @SerializedName("name")
    public final String name;

    @SerializedName("status")
    public final String status;

    @SerializedName("ordering")
    public final String ordering;

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

    @Embedded
    @SerializedName("default_photo")
    public final Image defaultPhoto;

    @Embedded(prefix = "icon_")
    @SerializedName("default_icon")
    public final Image defaultIcon;

    public SubCategory(@NonNull String id, String catId, String shopId, String name, String status, String ordering, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String updatedFlag, String addedDateStr, Image defaultPhoto, Image defaultIcon) {
        this.id = id;
        this.catId = catId;
        this.shopId = shopId;
        this.name = name;
        this.status = status;
        this.ordering = ordering;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.addedDateStr = addedDateStr;
        this.defaultPhoto = defaultPhoto;
        this.defaultIcon = defaultIcon;
    }
}
