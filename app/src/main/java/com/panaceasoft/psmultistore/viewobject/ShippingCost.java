package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

public class ShippingCost {

    @SerializedName("shipping")
    public final ShippingZone shippingZone;

    public ShippingCost( ShippingZone shippingZone) {
        this.shippingZone = shippingZone;
    }
}
