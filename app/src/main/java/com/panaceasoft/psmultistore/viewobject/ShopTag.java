package com.panaceasoft.psmultistore.viewobject;


import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ShopTag {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    public final String id;

    @SerializedName("name")
    public final String name;

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

    @Embedded(prefix = "default_photo")
    @SerializedName("default_photo")
    public final Image defaultPhoto;

    @Embedded(prefix = "default_icon")
    @SerializedName("default_icon")
    public final Image defaultIcon;

    public ShopTag(@NonNull String id, String name, String addedDate, String addedUserId, String status, String updatedDate, String updatedUserId, String addedDateStr, Image defaultPhoto, Image defaultIcon) {
        this.id = id;
        this.name = name;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.status = status;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.addedDateStr = addedDateStr;
        this.defaultPhoto = defaultPhoto;
        this.defaultIcon = defaultIcon;
    }
}
