package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShippingCostContainer {

    @SerializedName("country_id")
    public final String countryId;

    @SerializedName("city_id")
    public final String cityId;

    @SerializedName("shop_id")
    public final String shopId;

    @SerializedName("products")
    public final List<ShippingProductContainer> shippingProductContainerList;

    public ShippingCostContainer( String countryId, String cityId, String shopId, List<ShippingProductContainer> shippingProductContainerList) {
        this.countryId = countryId;
        this.cityId = cityId;
        this.shopId = shopId;
        this.shippingProductContainerList = shippingProductContainerList;
    }
}
