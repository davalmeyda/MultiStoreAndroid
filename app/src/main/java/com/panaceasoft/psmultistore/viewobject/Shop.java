package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(primaryKeys = "id")
public class Shop implements Serializable {

    @NonNull
    @SerializedName("id")
    public String id;

    @SerializedName("shipping_id")
    public String shippingId;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("coordinate")
    public String coordinate;

    @SerializedName("lat")
    public String lat;

    @SerializedName("lng")
    public String lng;

    @SerializedName("paypal_email")
    public String paypalEmail;

    @SerializedName("paypal_environment")
    public String paypalEnvironment;

    @SerializedName("paypal_appid_live")
    public String paypalAppidLive;

    @SerializedName("paypal_merchantname")
    public String paypalMerchantName;

    @SerializedName("paypal_customerid")
    public String paypalCustomerId;

    @SerializedName("paypal_ipnurl")
    public String paypalIpnurl;

    @SerializedName("paypal_memo")
    public String paypalMemo;

    @SerializedName("paypal_merchant_id")
    public String paypalMerchantId;

    @SerializedName("email")
    public String email;

    @SerializedName("paypal_public_key")
    public String paypalPublicKey;

    @SerializedName("paypal_private_key")
    public String paypalPrivateKey;

    @SerializedName("bank_account")
    public String bankAccount;

    @SerializedName("bank_name")
    public String bankName;

    @SerializedName("bank_code")
    public String bankCode;

    @SerializedName("branch_code")
    public String branchCode;

    @SerializedName("swift_code")
    public String swiftCode;

    @SerializedName("cod_email")
    public String codEmail;

    @SerializedName("stripe_publishable_key")
    public String stripePublishableKey;

    @SerializedName("stripe_secret_key")
    public String stripeSecretKey;

    @SerializedName("currency_symbol")
    public String currencySymbol;

    @SerializedName("currency_short_form")
    public String currencyShortForm;

    @SerializedName("sender_email")
    public String senderEmail;

    @SerializedName("added_date")
    public String added;

    @SerializedName("status")
    public String status;

    @SerializedName("paypal_enabled")
    public String paypalEnabled;

    @SerializedName("stripe_enabled")
    public String stripeEnabled;

    @SerializedName("cod_enabled")
    public String codEnabled;

    @SerializedName("banktransfer_enabled")
    public String banktransferEnabled;

    @SerializedName("is_featured")
    public String isFeatured;

    @SerializedName("pinterest")
    public final String pinterest;

    @SerializedName("youtube")
    public final String youtube;

    @SerializedName("instagram")
    public final String instagram;

    @SerializedName("twitter")
    public final String twitter;

    @SerializedName("google_plus")
    public final String googlePlus;

    @SerializedName("facebook")
    public final String facebook;

    @SerializedName("about_website")
    public final String aboutWebsite;

    @SerializedName("about_phone1")
    public final String aboutPhone1;

    @SerializedName("about_phone2")
    public final String aboutPhone2;

    @SerializedName("about_phone3")
    public final String aboutPhone3;

    @SerializedName("overall_tax_label")
    public String overallTaxLabel;

    @SerializedName("overall_tax_value")
    public float overallTaxValue;

    @SerializedName("shipping_tax_label")
    public String shippingTaxLabel;

    @SerializedName("shipping_tax_value")
    public float shippingTaxValue;

    @SerializedName("whapsapp_no")
    public String whapsappNo;

    @SerializedName("refund_policy")
    public String refundPolicy;

    @SerializedName("privacy_policy")
    public String privacyPolicy;

    @SerializedName("terms")
    public String terms;

    @SerializedName("address1")
    public String address1;

    @SerializedName("address2")
    public String address2;

    @SerializedName("address3")
    public String address3;

    @SerializedName("added_user_id")
    public String addedUserId;

    @SerializedName("updated_date")
    public String updatedDate;

    @SerializedName("updated_user_id")
    public String updatedUserId;

    @SerializedName("featured_date")
    public String featuredDate;

    @SerializedName("added_date_str")
    public String addedDateStr;

    @Embedded(prefix = "default_photo")
    @SerializedName("default_photo")
    public Image defaultPhoto;

    @Embedded(prefix = "default_icon")
    @SerializedName("default_icon")
    public Image defaultIcon;

    @SerializedName("touch_count")
    public String touchCount;

    @SerializedName("messenger")
    public String messenger;

    @SerializedName("standard_shipping_enable")
    public String standardShippingEnable;

    @SerializedName("zone_shipping_enable")
    public String zoneShippingEnable;

    @SerializedName("no_shipping_enable")
    public String noShippingEnable;

    public Shop(@NonNull String id, String shippingId, String name, String description, String coordinate, String lat, String lng, String paypalEmail, String paypalEnvironment, String paypalAppidLive, String paypalMerchantName, String paypalCustomerId, String paypalIpnurl, String paypalMemo, String paypalMerchantId, String email, String paypalPublicKey, String paypalPrivateKey, String bankAccount, String bankName, String bankCode, String branchCode, String swiftCode, String codEmail, String stripePublishableKey, String stripeSecretKey, String currencySymbol, String currencyShortForm, String senderEmail, String added, String status, String paypalEnabled, String stripeEnabled, String codEnabled, String banktransferEnabled, String isFeatured, String pinterest, String youtube, String instagram, String twitter, String googlePlus, String facebook, String aboutWebsite, String aboutPhone1, String aboutPhone2, String aboutPhone3, String overallTaxLabel, float overallTaxValue, String shippingTaxLabel, float shippingTaxValue, String whapsappNo, String refundPolicy, String privacyPolicy, String terms, String address1, String address2, String address3, String addedUserId, String updatedDate, String updatedUserId, String featuredDate, String addedDateStr, Image defaultPhoto, Image defaultIcon, String touchCount, String messenger, String standardShippingEnable, String zoneShippingEnable, String noShippingEnable) {
        this.id = id;
        this.shippingId = shippingId;
        this.name = name;
        this.description = description;
        this.coordinate = coordinate;
        this.lat = lat;
        this.lng = lng;
        this.paypalEmail = paypalEmail;
        this.paypalEnvironment = paypalEnvironment;
        this.paypalAppidLive = paypalAppidLive;
        this.paypalMerchantName = paypalMerchantName;
        this.paypalCustomerId = paypalCustomerId;
        this.paypalIpnurl = paypalIpnurl;
        this.paypalMemo = paypalMemo;
        this.paypalMerchantId = paypalMerchantId;
        this.email = email;
        this.paypalPublicKey = paypalPublicKey;
        this.paypalPrivateKey = paypalPrivateKey;
        this.bankAccount = bankAccount;
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.branchCode = branchCode;
        this.swiftCode = swiftCode;
        this.codEmail = codEmail;
        this.stripePublishableKey = stripePublishableKey;
        this.stripeSecretKey = stripeSecretKey;
        this.currencySymbol = currencySymbol;
        this.currencyShortForm = currencyShortForm;
        this.senderEmail = senderEmail;
        this.added = added;
        this.status = status;
        this.paypalEnabled = paypalEnabled;
        this.stripeEnabled = stripeEnabled;
        this.codEnabled = codEnabled;
        this.banktransferEnabled = banktransferEnabled;
        this.isFeatured = isFeatured;
        this.pinterest = pinterest;
        this.youtube = youtube;
        this.instagram = instagram;
        this.twitter = twitter;
        this.googlePlus = googlePlus;
        this.facebook = facebook;
        this.aboutWebsite = aboutWebsite;
        this.aboutPhone1 = aboutPhone1;
        this.aboutPhone2 = aboutPhone2;
        this.aboutPhone3 = aboutPhone3;
        this.overallTaxLabel = overallTaxLabel;
        this.overallTaxValue = overallTaxValue;
        this.shippingTaxLabel = shippingTaxLabel;
        this.shippingTaxValue = shippingTaxValue;
        this.whapsappNo = whapsappNo;
        this.refundPolicy = refundPolicy;
        this.privacyPolicy = privacyPolicy;
        this.terms = terms;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.featuredDate = featuredDate;
        this.addedDateStr = addedDateStr;
        this.defaultPhoto = defaultPhoto;
        this.defaultIcon = defaultIcon;
        this.touchCount = touchCount;
        this.messenger = messenger;
        this.standardShippingEnable = standardShippingEnable;
        this.zoneShippingEnable = zoneShippingEnable;
        this.noShippingEnable = noShippingEnable;
    }
}
