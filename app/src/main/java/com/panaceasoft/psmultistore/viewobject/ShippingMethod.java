package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ShippingMethod {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    public final String id;

    @SerializedName("shop_id")
    public final String shopId;

    @SerializedName("price")
    public final float price;

    @SerializedName("name")
    public final String name;

    @SerializedName("days")
    public final String days;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("updated_date")
    public final String updatedDate;

    @SerializedName("added_user_id")
    public final String addedUserId;

    @SerializedName("updated_user_id")
    public final String updatedUserId;

    @SerializedName("updated_flag")
    public final String updatedFlag;

    @SerializedName("is_published")
    public final String isPublished;

    @SerializedName("added_date_str")
    public final String addedDateStr;

    @SerializedName("currency_short_form")
    public final String currencyShortForm;

    @SerializedName("currency_symbol")
    public final String currencySymbol;

    public ShippingMethod(@NonNull String id, String shopId, float price, String name, String days, String addedDate, String updatedDate, String addedUserId, String updatedUserId, String updatedFlag, String isPublished, String addedDateStr, String currencyShortForm, String currencySymbol) {
        this.id = id;
        this.shopId = shopId;
        this.price = price;
        this.name = name;
        this.days = days;
        this.addedDate = addedDate;
        this.updatedDate = updatedDate;
        this.addedUserId = addedUserId;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.isPublished = isPublished;
        this.addedDateStr = addedDateStr;
        this.currencyShortForm = currencyShortForm;
        this.currencySymbol = currencySymbol;
    }
}
