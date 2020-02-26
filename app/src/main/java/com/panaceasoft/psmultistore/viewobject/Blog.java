package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Blog {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    public final String id;

    @SerializedName("shop_id")
    public final String shopId;

    @SerializedName("name")
    public final String name;

    @SerializedName("description")
    public final String description;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("added_user_id")
    public final String addedUserId;

    @SerializedName("updated_date")
    public final String updatedDate;

    @SerializedName("updated_user_id")
    public final String updatedUserId;

    @SerializedName("status")
    public final String status;

    @SerializedName("added_date_str")
    public final String addedDateStr;

    @Embedded(prefix = "default_photo_")
    @SerializedName("default_photo")
    public final Image defaultPhoto;

    @Embedded(prefix = "default_icon")
    @SerializedName("default_icon")
    public Image defaultIcon;

    @Embedded(prefix = "shop_default_")
    @SerializedName("shop")
    public Shop shop;

    public Blog(@NonNull String id, String shopId, String name, String description, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String status, String addedDateStr, Image defaultPhoto,Image defaultIcon) {
        this.id = id;
        this.shopId = shopId;
        this.name = name;
        this.description = description;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.status = status;
        this.addedDateStr = addedDateStr;
        this.defaultPhoto = defaultPhoto;
        this.defaultIcon = defaultIcon;
    }
}
