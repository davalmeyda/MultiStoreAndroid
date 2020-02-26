package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = "id")
public class TransactionDetail {

    @NonNull
    @SerializedName("id")
    public String id;

    @SerializedName("transactions_header_id")
    public String transactionsHeaderId;

    @SerializedName("product_id")
    public String productId;

    @SerializedName("product_name")
    public String productName;

    @SerializedName("product_attribute_id")
    public String productAttributeId;

    @SerializedName("product_attribute_name")
    public String productAttributeName;

    @SerializedName("original_price")
    public float originalPrice;

    @SerializedName("price")
    public float price;

    @SerializedName("discount_amount")
    public float discountAvailableAmount;

    @SerializedName("qty")
    public String qty;

    @SerializedName("color_id")
    public String colorId;

    @SerializedName("discount_value")
    public float discountValue;

    @SerializedName("discount_percent")
    public float discountPercent;

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

    @SerializedName("currency_symbol")
    public String currencySymbol;

    @SerializedName("currency_short_form")
    public String currencyShortForm;

    @SerializedName("added_date_str")
    public String addedDateStr;

    @SerializedName("updated_date_str")
    public String updatedDateStr;

    @SerializedName("product_color_id")
    public String productColorId;

    @SerializedName("product_color_code")
    public String productColorCode;

    @SerializedName("product_measurement")
    public String productMeasurement;

    @SerializedName("product_unit")
    public String productUnit;

    @SerializedName("shipping_cost")
    public String shippingCost;

    public TransactionDetail(@NonNull String id, String transactionsHeaderId, String productId, String productName, String productAttributeId, String productAttributeName, float originalPrice, float price, float discountAvailableAmount, String qty, String colorId, float discountValue, float discountPercent, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String updatedFlag, String currencySymbol, String currencyShortForm, String addedDateStr, String updatedDateStr, String productColorId, String productColorCode, String productMeasurement, String productUnit, String shippingCost) {
        this.id = id;
        this.transactionsHeaderId = transactionsHeaderId;
        this.productId = productId;
        this.productName = productName;
        this.productAttributeId = productAttributeId;
        this.productAttributeName = productAttributeName;
        this.originalPrice = originalPrice;
        this.price = price;
        this.discountAvailableAmount = discountAvailableAmount;
        this.qty = qty;
        this.colorId = colorId;
        this.discountValue = discountValue;
        this.discountPercent = discountPercent;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.currencySymbol = currencySymbol;
        this.currencyShortForm = currencyShortForm;
        this.addedDateStr = addedDateStr;
        this.updatedDateStr = updatedDateStr;
        this.productColorId = productColorId;
        this.productColorCode = productColorCode;
        this.productMeasurement = productMeasurement;
        this.productUnit = productUnit;
        this.shippingCost = shippingCost;
    }
}
