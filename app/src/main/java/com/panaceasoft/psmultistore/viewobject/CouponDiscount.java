package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;

@Entity
public class CouponDiscount {

    @SerializedName("id")
    public String id;

    @SerializedName("shop_id")
    public String shopId;

    @SerializedName("coupon_name")
    public String couponName;

    @SerializedName("coupon_code")
    public String couponCode;

    @SerializedName("coupon_amount")
    public String couponAmount;

    @SerializedName("is_published")
    public String isPublished;

    @SerializedName("added_date")
    public String addedDate;

    @SerializedName("updated_date")
    public String updatedDate;

    @SerializedName("added_user_id")
    public String addedUserId;

    @SerializedName("updated_user_id")
    public String updatedUserId;

    @SerializedName("updated_flag")
    public String updatedFlag;

    @SerializedName("added_date_str")
    public String addedDateStr;

    public CouponDiscount(String id, String shopId, String couponName, String couponCode, String couponAmount, String isPublished, String addedDate, String updatedDate, String addedUserId, String updatedUserId, String updatedFlag, String addedDateStr) {
        this.id = id;
        this.shopId = shopId;
        this.couponName = couponName;
        this.couponCode = couponCode;
        this.couponAmount = couponAmount;
        this.isPublished = isPublished;
        this.addedDate = addedDate;
        this.updatedDate = updatedDate;
        this.addedUserId = addedUserId;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.addedDateStr = addedDateStr;
    }
}
