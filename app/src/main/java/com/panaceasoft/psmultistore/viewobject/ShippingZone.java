package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

public class ShippingZone {

    @SerializedName("shipping_zone_package_name")
    public final String shippingZonePackageName;

    @SerializedName("shipping_cost")
    public final String shippingCost;

    public ShippingZone( String shippingZonePackageName, String shippingCost) {

        this.shippingZonePackageName = shippingZonePackageName;
        this.shippingCost = shippingCost;
    }
}
