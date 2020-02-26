package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.room.Entity;

@Entity
public class TransactionHeaderUpload {

    @SerializedName("user_id")
    public final String userId;

    @SerializedName("shop_id")
    public final String shopId;

    @SerializedName("sub_total_amount")
    public final String sub_total_amount;

    @SerializedName("discount_amount")
    public final String discountAmount;

    @SerializedName("coupon_discount_amount")
    public final String coupon_discount_amount;

    @SerializedName("tax_amount")
    public final String tax_amount;

    @SerializedName("shipping_amount")
    public final String shipping_amount;

    @SerializedName("balance_amount")
    public final String balance_amount;

    @SerializedName("total_item_amount")
    public final String total_item_amount;

    @SerializedName("contact_name")
    public final String contact_name;

    @SerializedName("contact_phone")
    public final String contact_phone;

    @SerializedName("is_cod")
    public final String is_cod;

    @SerializedName("is_paypal")
    public final String is_paypal;

    @SerializedName("is_stripe")
    public final String is_stripe;

    @SerializedName("is_bank")
    public final String is_bank;

    @SerializedName("payment_method_nonce")
    public final String payment_method_nonce;

    @SerializedName("trans_status_id")
    public final String trans_status_id;

    @SerializedName("currency_symbol")
    public final String currencySymbol;

    @SerializedName("currency_short_form")
    public final String currencyShortForm;

    @SerializedName("billing_first_name")
    public final String billing_first_name;

    @SerializedName("billing_last_name")
    public final String billing_last_name;

    @SerializedName("billing_company")
    public final String billing_company;

    @SerializedName("billing_address_1")
    public final String billing_address_1;

    @SerializedName("billing_address_2")
    public final String billing_address_2;

    @SerializedName("billing_country")
    public final String billing_country;

    @SerializedName("billing_state")
    public final String billing_state;

    @SerializedName("billing_city")
    public final String billing_city;

    @SerializedName("billing_postal_code")
    public final String billing_postal_code;

    @SerializedName("billing_email")
    public final String billing_email;

    @SerializedName("billing_phone")
    public final String billing_phone;

    @SerializedName("shipping_first_name")
    public final String shipping_first_name;

    @SerializedName("shipping_last_name")
    public final String shipping_last_name;

    @SerializedName("shipping_company")
    public final String shipping_company;

    @SerializedName("shipping_address_1")
    public final String shipping_address_1;

    @SerializedName("shipping_address_2")
    public final String shipping_address_2;

    @SerializedName("shipping_country")
    public final String shipping_country;

    @SerializedName("shipping_state")
    public final String shipping_state;

    @SerializedName("shipping_city")
    public final String shipping_city;

    @SerializedName("shipping_postal_code")
    public final String shipping_postal_code;

    @SerializedName("shipping_email")
    public final String shipping_email;

    @SerializedName("shipping_phone")
    public final String shipping_phone;

    @SerializedName("shipping_tax_percent")
    public final String shipping_tax_percent;

    @SerializedName("tax_percent")
    public final String tax_percent;

    @SerializedName("shipping_method_amount")
    public final String shipping_method_amount;

    @SerializedName("shipping_method_name")
    public final String shipping_method_name;

    @SerializedName("memo")
    public final String memo;

    @SerializedName("is_zone_shipping")
    public final String isZoneShipping;

    @SerializedName("total_item_count")
    public final String totalItemCount;

    @SerializedName("details")
    public final List<BasketProductToServer> details;

    public TransactionHeaderUpload(String userId, String shopId, String sub_total_amount, String discountAmount, String coupon_discount_amount, String tax_amount, String shipping_amount, String balance_amount, String total_item_amount, String contact_name, String contact_phone, String is_cod, String is_paypal, String is_stripe, String is_bank, String payment_method_nonce, String trans_status_id, String currencySymbol, String currencyShortForm, String billing_first_name, String billing_last_name, String billing_company, String billing_address_1, String billing_address_2, String billing_country, String billing_state, String billing_city, String billing_postal_code, String billing_email, String billing_phone, String shipping_first_name, String shipping_last_name, String shipping_company, String shipping_address_1, String shipping_address_2, String shipping_country, String shipping_state, String shipping_city, String shipping_postal_code, String shipping_email, String shipping_phone, String shipping_tax_percent, String tax_percent, String shipping_method_amount, String shipping_method_name, String memo, String isZoneShipping, String totalItemCount, List<BasketProductToServer> details) {
        this.userId = userId;
        this.shopId = shopId;
        this.sub_total_amount = sub_total_amount;
        this.discountAmount = discountAmount;
        this.coupon_discount_amount = coupon_discount_amount;
        this.tax_amount = tax_amount;
        this.shipping_amount = shipping_amount;
        this.balance_amount = balance_amount;
        this.total_item_amount = total_item_amount;
        this.contact_name = contact_name;
        this.contact_phone = contact_phone;
        this.is_cod = is_cod;
        this.is_paypal = is_paypal;
        this.is_stripe = is_stripe;
        this.is_bank = is_bank;
        this.payment_method_nonce = payment_method_nonce;
        this.trans_status_id = trans_status_id;
        this.currencySymbol = currencySymbol;
        this.currencyShortForm = currencyShortForm;
        this.billing_first_name = billing_first_name;
        this.billing_last_name = billing_last_name;
        this.billing_company = billing_company;
        this.billing_address_1 = billing_address_1;
        this.billing_address_2 = billing_address_2;
        this.billing_country = billing_country;
        this.billing_state = billing_state;
        this.billing_city = billing_city;
        this.billing_postal_code = billing_postal_code;
        this.billing_email = billing_email;
        this.billing_phone = billing_phone;
        this.shipping_first_name = shipping_first_name;
        this.shipping_last_name = shipping_last_name;
        this.shipping_company = shipping_company;
        this.shipping_address_1 = shipping_address_1;
        this.shipping_address_2 = shipping_address_2;
        this.shipping_country = shipping_country;
        this.shipping_state = shipping_state;
        this.shipping_city = shipping_city;
        this.shipping_postal_code = shipping_postal_code;
        this.shipping_email = shipping_email;
        this.shipping_phone = shipping_phone;
        this.shipping_tax_percent = shipping_tax_percent;
        this.tax_percent = tax_percent;
        this.shipping_method_amount = shipping_method_amount;
        this.shipping_method_name = shipping_method_name;
        this.memo = memo;
        this.isZoneShipping = isZoneShipping;
        this.totalItemCount = totalItemCount;
        this.details = details;
    }
}



/**

 package com.panaceasoft.mokets.viewobject;

 import com.google.gson.annotations.SerializedName;

 import java.util.List;

 import androidx.room.Entity;

 @Entity
 public class TransactionHeaderUpload {

 @SerializedName("user_id")
 public final String userId;

 @SerializedName("shop_id")
 public final String shopId;

 @SerializedName("sub_total_amount")
 public final String subTotalAmount;

 @SerializedName("discount_amount")
 public final String discountAmount;

 @SerializedName("coupon_discount_amount")
 public final String couponDiscountAmount;

 @SerializedName("tax_amount")
 public final String taxAmount;

 @SerializedName("shipping_amount")
 public final String shippingAmount;

 @SerializedName("balance_amount")
 public final String balanceAmount;

 @SerializedName("total_item_amount")
 public final String totalItemAmount;

 @SerializedName("contact_name")
 public final String contactName;

 @SerializedName("contact_phone")
 public final String contactPhone;

 @SerializedName("is_cod")
 public final String is_cod;

 @SerializedName("is_paypal")
 public final String is_paypal;

 @SerializedName("is_stripe")
 public final String is_stripe;

 @SerializedName("payment_method_nonce")
 public final String payment_method_nonce;

 @SerializedName("trans_status_id")
 public final String transStatusId;

 @SerializedName("currency_symbol")
 public final String currencySymbol;

 @SerializedName("currency_short_form")
 public final String currencyShortForm;

 @SerializedName("billing_first_name")
 public final String billingFirstName;

 @SerializedName("billing_last_name")
 public final String billingLastName;

 @SerializedName("billing_company")
 public final String billingCompany;

 @SerializedName("billing_address_1")
 public final String billingAddress1;

 @SerializedName("billing_address_2")
 public final String billingAddress2;

 @SerializedName("billing_country")
 public final String billingCountry;

 @SerializedName("billing_state")
 public final String billingState;

 @SerializedName("billing_city")
 public final String billingCity;

 @SerializedName("billing_postal_code")
 public final String billingPostalCode;

 @SerializedName("billing_email")
 public final String billingEmail;

 @SerializedName("billing_phone")
 public final String billingPhone;

 @SerializedName("shipping_first_name")
 public final String shippingFirstName;

 @SerializedName("shipping_last_name")
 public final String shippingLastName;

 @SerializedName("shipping_company")
 public final String shippingCompany;

 @SerializedName("shipping_address_1")
 public final String shippingAddress1;

 @SerializedName("shipping_address_2")
 public final String shippingAddress2;

 @SerializedName("shipping_country")
 public final String shippingCountry;

 @SerializedName("shipping_state")
 public final String shippingState;

 @SerializedName("shipping_city")
 public final String shippingCity;

 @SerializedName("shipping_postal_code")
 public final String shippingPostalCode;

 @SerializedName("shipping_email")
 public final String shippingEmail;

 @SerializedName("shipping_phone")
 public final String shippingPhone;

 @SerializedName("details")
 public final List<BasketProductToServer> details;

 public TransactionHeaderUpload(String userId, String shopId, String subTotalAmount, String discountAmount, String couponDiscountAmount, String taxAmount, String shippingAmount, String balanceAmount, String totalItemAmount, String contactName, String contactPhone, String is_cod, String is_paypal, String is_stripe, String payment_method_nonce, String transStatusId, String currencySymbol, String currencyShortForm, String billingFirstName, String billingLastName, String billingCompany, String billingAddress1, String billingAddress2, String billingCountry, String billingState, String billingCity, String billingPostalCode, String billingEmail, String billingPhone, String shippingFirstName, String shippingLastName, String shippingCompany, String shippingAddress1, String shippingAddress2, String shippingCountry, String shippingState, String shippingCity, String shippingPostalCode, String shippingEmail, String shippingPhone, List<BasketProductToServer> details) {
 this.userId = userId;
 this.shopId = shopId;
 this.subTotalAmount = subTotalAmount;
 this.discountAmount = discountAmount;
 this.couponDiscountAmount = couponDiscountAmount;
 this.taxAmount = taxAmount;
 this.shippingAmount = shippingAmount;
 this.balanceAmount = balanceAmount;
 this.totalItemAmount = totalItemAmount;
 this.contactName = contactName;
 this.contactPhone = contactPhone;
 this.is_cod = is_cod;
 this.is_paypal = is_paypal;
 this.is_stripe = is_stripe;
 this.payment_method_nonce = payment_method_nonce;
 this.transStatusId = transStatusId;
 this.currencySymbol = currencySymbol;
 this.currencyShortForm = currencyShortForm;
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
 this.details = details;
 }
 }
 */