package com.panaceasoft.psmultistore.viewobject.holder;

import com.panaceasoft.psmultistore.utils.Constants;

public class CategoryParameterHolder {

    public String order_by, shopId;

    public CategoryParameterHolder() {

        this.order_by = Constants.FILTERING_ADDED_DATE;
        this.shopId = "";

    }

    public CategoryParameterHolder getTrendingCategories()
    {
        this.shopId = "";
        this.order_by = Constants.FILTERING_TRENDING;

        return this;
    }

    public String changeToMapValue() {
        String result = "";

        if (!shopId.isEmpty()) {
            result += shopId;
        }

        if (!order_by.isEmpty()) {
            result += order_by;
        }

        return result;
    }
}
