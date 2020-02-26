package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(primaryKeys = "id")
public class ProductAttributeHeader {
    @SerializedName("id")
    @NonNull
    public final String id;

    @SerializedName("product_id")
    public final String productId;

    @SerializedName("shop_id")
    public final String shopId;

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

    @SerializedName("updated_flag")
    public final String updatedFlag;

    @SerializedName("is_empty_object")
    public final String isEmptyObject;

    @SerializedName("attributes_detail")
    @Ignore
    public List<ProductAttributeDetail> attributesDetailList;

    public ProductAttributeHeader(@NonNull String id, String productId, String shopId, String name, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String updatedFlag, String isEmptyObject) {
        this.id = id;
        this.productId = productId;
        this.shopId = shopId;
        this.name = name;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.isEmptyObject = isEmptyObject;
    }
}
