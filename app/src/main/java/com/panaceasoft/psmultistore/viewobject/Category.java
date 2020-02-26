package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

/**
 * Created by Panacea-Soft on 11/25/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Entity(primaryKeys = "id")
public class Category {


    @NonNull
    @SerializedName("id")
    public String id;

    @SerializedName("shop_id")
    public String shopId;

    @SerializedName("name")
    public String name;

    @SerializedName("status")
    public String status;

    @SerializedName("added_date")
    public String addedDate;

    @SerializedName("added_user_id")
    public String addedUserId;

    @SerializedName("updated_date")
    public String updatedDate;

    @SerializedName("updated_user_id")
    public String updatedUserId;

    @SerializedName("updated_flag")
    public String updatedFlag;

    @SerializedName("added_date_str")
    public String addedDateStr;

    @SerializedName("touch_count")
    public String touchCount;

    @Embedded(prefix = "default_photo")
    @SerializedName("default_photo")
    public Image defaultPhoto;

    @Embedded(prefix = "default_icon")
    @SerializedName("default_icon")
    public Image defaultIcon;

    public Category(@NonNull String id, String shopId, String name, String status, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String updatedFlag, String addedDateStr, String touchCount, Image defaultPhoto, Image defaultIcon) {
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
        this.touchCount = touchCount;
        this.defaultPhoto = defaultPhoto;
        this.defaultIcon = defaultIcon;
    }
}
