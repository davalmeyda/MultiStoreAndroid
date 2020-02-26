package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(primaryKeys = "id")
public class ProductAttributeDetail {

    @SerializedName("id")
    @NonNull
    public final String id;

    @SerializedName("shop_id")
    public final String shopId;

    @SerializedName("header_id")
    public final String headerId;

    @SerializedName("product_id")
    public final String productId;

    @SerializedName("name")
    public final String name;

    @SerializedName("additional_price")
    public String additionalPrice;

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

    @SerializedName("is_empty_object")
    public String isEmptyObject;

    @Ignore
    public String additionalPriceWithCurrency = "";

    public ProductAttributeDetail(@NonNull String id, String shopId, String headerId, String productId, String name, String additionalPrice, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String updatedFlag, String isEmptyObject) {
        this.id = id;
        this.shopId = shopId;
        this.headerId = headerId;
        this.productId = productId;
        this.name = name;
        this.additionalPrice = additionalPrice;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.isEmptyObject = isEmptyObject;
    }
}
