package com.panaceasoft.psmultistore.viewobject;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity
public class ShopToServer {

    @SerializedName("overall_tax_label")
    public String overallTaxLabel;

    @SerializedName("overall_tax_value")
    public String overallTaxValue;

    @SerializedName("shipping_tax_label")
    public String shippingTaxLabel;

    @SerializedName("shipping_tax_value")
    public String shippingTaxValue;

    public ShopToServer(String overallTaxLabel, String overallTaxValue, String shippingTaxLabel, String shippingTaxValue) {
        this.overallTaxLabel = overallTaxLabel;
        this.overallTaxValue = overallTaxValue;
        this.shippingTaxLabel = shippingTaxLabel;
        this.shippingTaxValue = shippingTaxValue;
    }
}
