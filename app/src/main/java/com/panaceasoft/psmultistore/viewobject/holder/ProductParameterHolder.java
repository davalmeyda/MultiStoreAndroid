package com.panaceasoft.psmultistore.viewobject.holder;

import com.panaceasoft.psmultistore.utils.Constants;

import java.io.Serializable;

public class ProductParameterHolder implements Serializable {

    public String shopId,
            search_term,
            catId,
            subCatId,
            isFeatured,
            isDiscount,
            isAvailable,
            max_price,
            min_price,
            overall_rating,
            rating_value_one,
            rating_value_two,
            rating_value_three,
            rating_value_four,
            rating_value_five,
            order_by,
            order_type;

    public ProductParameterHolder() {
        this.shopId = "";
        this.search_term = "";
        this.catId = "";
        this.subCatId = "";
        this.isFeatured = "";
        this.isDiscount = "";
        this.isAvailable = "";
        this.max_price = "";
        this.overall_rating = "";
        this.min_price = "";
        this.rating_value_one = "";
        this.rating_value_two = "";
        this.rating_value_three = "";
        this.rating_value_four = "";
        this.rating_value_five = "";
        this.order_by = Constants.FILTERING_ADDED_DATE;
        this.order_type = Constants.FILTERING_DESC;
    }

    public ProductParameterHolder getLatestParameterHolder()
    {
        this.shopId = "";
        this.search_term = "";
        this.catId = "";
        this.subCatId = "";
        this.isFeatured = "";
        this.isDiscount = "";
        this.isAvailable = "";
        this.max_price = "";
        this.overall_rating = "";
        this.min_price = "";
        this.rating_value_one = "";
        this.rating_value_two = "";
        this.rating_value_three = "";
        this.rating_value_four = "";
        this.rating_value_five = "";
        this.order_by = Constants.FILTERING_ADDED_DATE;
        this.order_type = Constants.FILTERING_DESC;

        return this;
    }

    public ProductParameterHolder getFeaturedParameterHolder()
    {
        this.shopId = "";
        this.search_term = "";
        this.catId = "";
        this.subCatId = "";
        this.isFeatured = Constants.ONE;
        this.isDiscount = "";
        this.isAvailable = "";
        this.max_price = "";
        this.overall_rating = "";
        this.min_price = "";
        this.rating_value_one = "";
        this.rating_value_two = "";
        this.rating_value_three = "";
        this.rating_value_four = "";
        this.rating_value_five = "";
        this.order_by = Constants.FILTERING_FEATURE;
        this.order_type = Constants.FILTERING_DESC;

        return this;
    }

    public ProductParameterHolder getDiscountParameterHolder()
    {
        this.shopId = "";
        this.search_term = "";
        this.catId = "";
        this.subCatId = "";
        this.isFeatured = "";
        this.isDiscount = Constants.ONE;
        this.isAvailable = "";
        this.max_price = "";
        this.overall_rating = "";
        this.min_price = "";
        this.rating_value_one = "";
        this.rating_value_two = "";
        this.rating_value_three = "";
        this.rating_value_four = "";
        this.rating_value_five = "";
        this.order_by = Constants.FILTERING_ADDED_DATE;
        this.order_type = Constants.FILTERING_DESC;

        return this;
    }

    public ProductParameterHolder getTrendingParameterHolder()
    {
        this.shopId = "";
        this.search_term = "";
        this.catId = "";
        this.subCatId = "";
        this.isFeatured = "";
        this.isDiscount = "";
        this.isAvailable = "";
        this.max_price = "";
        this.overall_rating = "";
        this.min_price = "";
        this.rating_value_one = "";
        this.rating_value_two = "";
        this.rating_value_three = "";
        this.rating_value_four = "";
        this.rating_value_five = "";
        this.order_by = Constants.FILTERING_TRENDING;
        this.order_type = Constants.FILTERING_DESC;

        return this;
    }

    public void resetTheHolder()
    {
        this.shopId = "";
        this.search_term = "";
        this.catId = "";
        this.subCatId = "";
        this.isFeatured = "";
        this.isDiscount = "";
        this.isAvailable = "";
        this.max_price = "";
        this.overall_rating = "";
        this.min_price = "";
        this.rating_value_one = "";
        this.rating_value_two = "";
        this.rating_value_three = "";
        this.rating_value_four = "";
        this.rating_value_five = "";
        this.order_by = Constants.FILTERING_ADDED_DATE;
        this.order_type = Constants.FILTERING_DESC;
    }

    public String getKeyForProductMap()
    {

        final String discount = "discount";
        final String featured = "featured";
        final String available = "available";
        final String rating_one = "rating_one";
        final String rating_two = "rating_two";
        final String rating_three = "rating_three";
        final String rating_four = "rating_four";
        final String rating_five = "rating_five";


        String result = "";

        if(!shopId.isEmpty())
        {
            result += shopId + ":";
        }

        if(!search_term.isEmpty())
        {
            result += search_term + ":";
        }

        if(!catId.isEmpty())
        {
            result += catId + ":";
        }

        if(!subCatId.isEmpty())
        {
            result += subCatId + ":";
        }

        if(!isFeatured.isEmpty())
        {
            result += featured + ":";
        }

        if(!isDiscount.isEmpty())
        {
            result += discount + ":";
        }

        if(!isAvailable.isEmpty())
        {
            result += available + ":";
        }

        if(!max_price.isEmpty())
        {
            result += max_price + ":";
        }

        if(!overall_rating.isEmpty())
        {
            result += overall_rating + ":";
        }

        if(!min_price.isEmpty())
        {
            result += min_price + ":";
        }

        if(!rating_value_one.isEmpty())
        {
            result += rating_one + ":";
        }


        if(!rating_value_two.isEmpty())
        {
            result += rating_two + ":";
        }

        if(!rating_value_three.isEmpty())
        {
            result += rating_three + ":";
        }

        if(!rating_value_four.isEmpty())
        {
            result += rating_four + ":";
        }

        if(!rating_value_five.isEmpty())
        {
            result += rating_five + ":";
        }

        if(!order_by.isEmpty())
        {
            result += order_by + ":";
        }

        if(!order_type.isEmpty())
        {
            result += order_type;
        }

        return result;
    }
}
