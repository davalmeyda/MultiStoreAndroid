package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(primaryKeys = "id")
public class TransactionObject {

    @NonNull
    @SerializedName("id")
    public String id;

    @SerializedName("user_id")
    public String userId;

    @SerializedName("shop_id")
    public String shopId;

    @SerializedName("sub_total_amount")
    public String subTotalAmount;

    @SerializedName("discount_amount")
    public String discountAmount;

    @SerializedName("coupon_discount_amount")
    public String couponDiscountAmount;

    @SerializedName("tax_amount")
    public String taxAmount;

    @SerializedName("tax_percent")
    public String taxPercent;

    @SerializedName("shipping_amount")
    public String shippingAmount;

    @SerializedName("shipping_tax_percent")
    public String shippingTaxPercent;

    @SerializedName("shipping_method_amount")
    public String shippingMethodAmount;

    @SerializedName("shipping_method_name")
    public String shippingMethodName;

    @SerializedName("balance_amount")
    public String balanceAmount;

    @SerializedName("total_item_amount")
    public String totalItemAmount;

    @SerializedName("total_item_count")
    public String totalItemCount;

    @SerializedName("contact_name")
    public String contactName;

    @SerializedName("contact_phone")
    public String contactPhone;

    @SerializedName("payment_method")
    public String paymentMethod;

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

    @SerializedName("trans_status_id")
    public String transStatusId;

    @SerializedName("currency_symbol")
    public String currencySymbol;

    @SerializedName("currency_short_form")
    public String currencyShortForm;

    @SerializedName("trans_code")
    public String transCode;

    @SerializedName("billing_first_name")
    public String billingFirstName;

    @SerializedName("billing_last_name")
    public String billingLastName;

    @SerializedName("billing_company")
    public String billingCompany;

    @SerializedName("billing_address_1")
    public String billingAddress1;

    @SerializedName("billing_address_2")
    public String billingAddress2;

    @SerializedName("billing_country")
    public String billingCountry;

    @SerializedName("billing_state")
    public String billingState;

    @SerializedName("billing_city")
    public String billingCity;

    @SerializedName("billing_postal_code")
    public String billingPostalCode;

    @SerializedName("billing_email")
    public String billingEmail;

    @SerializedName("billing_phone")
    public String billingPhone;

    @SerializedName("shipping_first_name")
    public String shippingFirstName;

    @SerializedName("shipping_last_name")
    public String shippingLastName;

    @SerializedName("shipping_company")
    public String shippingCompany;

    @SerializedName("shipping_address_1")
    public String shippingAddress1;

    @SerializedName("shipping_address_2")
    public String shippingAddress2;

    @SerializedName("shipping_country")
    public String shippingCountry;

    @SerializedName("shipping_state")
    public String shippingState;

    @SerializedName("shipping_city")
    public String shippingCity;

    @SerializedName("shipping_postal_code")
    public String shippingPostalCode;

    @SerializedName("shipping_email")
    public String shippingEmail;

    @SerializedName("shipping_phone")
    public String shippingPhone;

    @SerializedName("memo")
    public String memo;

    @SerializedName("product_measurement")
    public String productMeasurement;

    @SerializedName("product_unit")
    public String productUnit;

    @SerializedName("is_zone_shipping")
    public String isZoneShipping;

    @SerializedName("added_date_str")
    public String addedDateStr;

    @SerializedName("trans_status_title")
    public String transStatusTitle;

    @Embedded(prefix = "default_shop_")
    @SerializedName("shop")
    public Shop shop;

    public TransactionObject(@NonNull String id, String userId, String shopId, String subTotalAmount, String discountAmount, String couponDiscountAmount, String taxAmount, String taxPercent, String shippingAmount, String shippingTaxPercent, String shippingMethodAmount, String shippingMethodName, String balanceAmount, String totalItemAmount, String totalItemCount, String contactName, String contactPhone, String paymentMethod, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String updatedFlag, String transStatusId, String currencySymbol, String currencyShortForm, String transCode, String billingFirstName, String billingLastName, String billingCompany, String billingAddress1, String billingAddress2, String billingCountry, String billingState, String billingCity, String billingPostalCode, String billingEmail, String billingPhone, String shippingFirstName, String shippingLastName, String shippingCompany, String shippingAddress1, String shippingAddress2, String shippingCountry, String shippingState, String shippingCity, String shippingPostalCode, String shippingEmail, String shippingPhone, String memo, String productMeasurement, String productUnit, String isZoneShipping, String addedDateStr, String transStatusTitle, Shop shop) {
        this.id = id;
        this.userId = userId;
        this.shopId = shopId;
        this.subTotalAmount = subTotalAmount;
        this.discountAmount = discountAmount;
        this.couponDiscountAmount = couponDiscountAmount;
        this.taxAmount = taxAmount;
        this.taxPercent = taxPercent;
        this.shippingAmount = shippingAmount;
        this.shippingTaxPercent = shippingTaxPercent;
        this.shippingMethodAmount = shippingMethodAmount;
        this.shippingMethodName = shippingMethodName;
        this.balanceAmount = balanceAmount;
        this.totalItemAmount = totalItemAmount;
        this.totalItemCount = totalItemCount;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.paymentMethod = paymentMethod;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.transStatusId = transStatusId;
        this.currencySymbol = currencySymbol;
        this.currencyShortForm = currencyShortForm;
        this.transCode = transCode;
        this.billingFirstName = billingFirstName;
        this.billingLastName = billingLastName;
        this.billingCompany = billingCompany;
        this.billingAddress1 = billingAddress1;
        this.billingAddress2 = billingAddress2;
        this.billingCountry = billingCountry;
        this.billingState = billingState;
        this.billingCity = billingCity;
        this.billingPostalCode = billingPostalCode;
        this.billingEmail = billingEmail;
        this.billingPhone = billingPhone;
        this.shippingFirstName = shippingFirstName;
        this.shippingLastName = shippingLastName;
        this.shippingCompany = shippingCompany;
        this.shippingAddress1 = shippingAddress1;
        this.shippingAddress2 = shippingAddress2;
        this.shippingCountry = shippingCountry;
        this.shippingState = shippingState;
        this.shippingCity = shippingCity;
        this.shippingPostalCode = shippingPostalCode;
        this.shippingEmail = shippingEmail;
        this.shippingPhone = shippingPhone;
        this.memo = memo;
        this.productMeasurement = productMeasurement;
        this.productUnit = productUnit;
        this.isZoneShipping = isZoneShipping;
        this.addedDateStr = addedDateStr;
        this.transStatusTitle = transStatusTitle;
        this.shop = shop;
    }
}

