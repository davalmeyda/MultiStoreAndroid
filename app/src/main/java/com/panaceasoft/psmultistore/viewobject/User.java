package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

/**
 * Created by Panacea-Soft on 12/6/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Entity(primaryKeys = "userId")
public class User {

    @NonNull
    @SerializedName("user_id")
    public String userId;

    @SerializedName("user_is_sys_admin")
    public String userIsSysAdmin;

    @SerializedName("is_shop_admin")
    public String isShopAdmin;

    @SerializedName("facebook_id")
    public String facebookId;

    @SerializedName("google_id")
    public String googleId;

    @SerializedName("user_name")
    public String userName;

    @SerializedName("user_email")
    public String userEmail;

    @SerializedName("user_phone")
    public String userPhone;

    @SerializedName("user_password")
    public String userPassword;

    @SerializedName("user_about_me")
    public String userAboutMe;

    @SerializedName("user_cover_photo")
    public String userCoverPhoto;

    @SerializedName("user_profile_photo")
    public String userProfilePhoto;

    @SerializedName("role_id")
    public String roleId;

    @SerializedName("status")
    public String status;

    @SerializedName("is_banned")
    public String isBanned;

    @SerializedName("added_date")
    public String addedDate;

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

    @SerializedName("device_token")
    public String deviceToken;

    @SerializedName("code")
    public String code;

    @SerializedName("verify_types")
    public String verifyTypes;

    @SerializedName("added_date_str")
    public String addedDateStr;

    @SerializedName("country")
    @Embedded(prefix = "country")
    public Country country;

    @SerializedName("city")
    @Embedded(prefix = "city")
    public City city;

    public User(@NonNull String userId, String userIsSysAdmin, String isShopAdmin, String facebookId, String googleId, String userName, String userEmail, String userPhone, String userPassword, String userAboutMe, String userCoverPhoto, String userProfilePhoto, String roleId, String status, String isBanned, String addedDate, String billingFirstName, String billingLastName, String billingCompany, String billingAddress1, String billingAddress2, String billingCountry, String billingState, String billingCity, String billingPostalCode, String billingEmail, String billingPhone, String shippingFirstName, String shippingLastName, String shippingCompany, String shippingAddress1, String shippingAddress2, String shippingCountry, String shippingState, String shippingCity, String shippingPostalCode, String shippingEmail, String shippingPhone, String deviceToken, String code, String verifyTypes, String addedDateStr, Country country, City city) {
        this.userId = userId;
        this.userIsSysAdmin = userIsSysAdmin;
        this.isShopAdmin = isShopAdmin;
        this.facebookId = facebookId;
        this.googleId = googleId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userPassword = userPassword;
        this.userAboutMe = userAboutMe;
        this.userCoverPhoto = userCoverPhoto;
        this.userProfilePhoto = userProfilePhoto;
        this.roleId = roleId;
        this.status = status;
        this.isBanned = isBanned;
        this.addedDate = addedDate;
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
        this.deviceToken = deviceToken;
        this.code = code;
        this.verifyTypes = verifyTypes;
        this.addedDateStr = addedDateStr;
        this.country = country;
        this.city = city;
    }
}
