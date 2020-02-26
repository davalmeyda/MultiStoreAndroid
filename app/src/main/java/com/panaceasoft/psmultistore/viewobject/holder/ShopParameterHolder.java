package com.panaceasoft.psmultistore.viewobject.holder;

import com.panaceasoft.psmultistore.utils.Constants;

import java.io.Serializable;

public class ShopParameterHolder implements Serializable {

    public String isFeature, orderBy, orderType;

    public ShopParameterHolder() {
        this.isFeature = "";
        this.orderBy = Constants.FILTERING_ADDED_DATE;
        this.orderType = Constants.FILTERING_DESC;
    }

    public ShopParameterHolder getLatestParameterHolder() {
        this.isFeature = "";
        this.orderBy = Constants.FILTERING_ADDED_DATE;
        this.orderType = Constants.FILTERING_DESC;

        return this;
    }

    public ShopParameterHolder getFeaturedParameterHolder() {
        this.isFeature = Constants.ONE;
        this.orderBy = Constants.FILTERING_FEATURE;
        this.orderType = Constants.FILTERING_DESC;

        return this;
    }

    public ShopParameterHolder getPopularParameterHolder() {
        this.isFeature = "";
        this.orderBy = Constants.FILTERING_TRENDING;
        this.orderType = Constants.FILTERING_DESC;

        return this;
    }

    public String getShopMapKey() {

        final String FEATURED = Constants.PRODUCT_FEATURED;

        String result = "";

        if (!isFeature.isEmpty()) {
            result += FEATURED + ":";
        }

        if (!orderBy.isEmpty()) {
            result += orderBy + ":";
        }

        if (!orderType.isEmpty()) {
            result += orderType;
        }

        return result;
    }

}
