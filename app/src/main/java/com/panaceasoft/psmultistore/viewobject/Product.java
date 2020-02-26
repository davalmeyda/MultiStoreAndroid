package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;

/**
 * Created by Panacea-Soft on 9/17/18.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Entity(primaryKeys = "id")
public class Product {

    @SerializedName("id")
    @NonNull
    public final String id;

    @SerializedName("shop_id")
    public final String shopId;

    @SerializedName("cat_id")
    public final String catId;

    @SerializedName("sub_cat_id")
    public final String subCatId;

    @SerializedName("is_discount")
    public final String isDiscount;

    @SerializedName("is_featured")
    public final String isFeatured;

    @SerializedName("is_available")
    public final String isAvailable;

    @SerializedName("code")
    public final String code;

    @SerializedName("name")
    public final String name;

    @SerializedName("description")
    public final String description;

    @SerializedName("search_tag")
    public final String searchTag;

    @SerializedName("highlight_information")
    public final String highlightInformation;

    @SerializedName("status")
    public final String status;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("added_user_id")
    public final String addedUserId;

    @SerializedName("updated_date")
    public final String updatedDate;

    @SerializedName("updated_user_id")
    public final String updatedUserId;

    @SerializedName("updated_flag")
    public final String updatedFlag;

    @SerializedName("deleted_flag")
    public final String deletedFlag;

    @SerializedName("added_date_str")
    public final String addedDateStr;

    @SerializedName("shipping_cost")
    public final String shippingCost;

    @SerializedName("minimum_order")
    public final String minimumOrder;

    @SerializedName("product_unit")
    public final String productUnit;

    @SerializedName("product_measurement")
    public final String productMeasurement;

    @Embedded
    @SerializedName("default_photo")
    public final Image defaultPhoto;

    @Embedded(prefix = "category_")
    @SerializedName("category")
    public final Category category;

    @Embedded(prefix = "subCategory_")
    @SerializedName("sub_category")
    public final SubCategory subCategory;

    @Embedded(prefix = "ratingDetails_")
    @SerializedName("rating_details")
    public RatingDetail ratingDetails;

    @SerializedName("like_count")
    public final int likeCount;

    @SerializedName("image_count")
    public final int imageCount;

    @SerializedName("favourite_count")
    public final int favouriteCount;

    @SerializedName("touch_count")
    public final int touchCount;

    @SerializedName("featured_date")
    public final String featuredDate;

    @SerializedName("comment_header_count")
    public final int commentHeaderCount;

    @SerializedName("original_price")
    public final float originalPrice;

    @SerializedName("unit_price")
    public final float unitPrice;

    @SerializedName("discount_amount")
    public final float discountAmount;

    @SerializedName("currency_short_form")
    public final String currencyShortForm;

    @SerializedName("currency_symbol")
    public final String currencySymbol;

    @SerializedName("discount_percent")
    public final float discountPercent;

    @SerializedName("discount_value")
    public final float discountValue;

    @SerializedName("is_liked")
    public final String isLiked;

    @SerializedName("is_favourited")
    public final String isFavourited;

    @SerializedName("overall_rating")
    public final String overallRating;

    @SerializedName("trans_status")
    public final String transStatus;

    @SerializedName("attributes_header")
    @Ignore
    public List<ProductAttributeHeader> attributesHeaderList;

    @SerializedName("colors")
    @Ignore
    public List<ProductColor> productColorList;

    @SerializedName("specs")
    @Ignore
    public List<ProductSpecs> productSpecsList;

    public Product(@NonNull String id, String shopId, String catId, String subCatId, String isDiscount, String isFeatured, String isAvailable, String code, String name, String description, String searchTag, String highlightInformation, String status, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String updatedFlag, String deletedFlag, String addedDateStr, String shippingCost, String minimumOrder, String productUnit, String productMeasurement, Image defaultPhoto, Category category, SubCategory subCategory, RatingDetail ratingDetails, int likeCount, int imageCount, int favouriteCount, int touchCount, String featuredDate, int commentHeaderCount, float originalPrice, float unitPrice, float discountAmount, String currencyShortForm, String currencySymbol, float discountPercent, float discountValue, String isLiked, String isFavourited, String overallRating, String transStatus) {
        this.id = id;
        this.shopId = shopId;
        this.catId = catId;
        this.subCatId = subCatId;
        this.isDiscount = isDiscount;
        this.isFeatured = isFeatured;
        this.isAvailable = isAvailable;
        this.code = code;
        this.name = name;
        this.description = description;
        this.searchTag = searchTag;
        this.highlightInformation = highlightInformation;
        this.status = status;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.deletedFlag = deletedFlag;
        this.addedDateStr = addedDateStr;
        this.shippingCost = shippingCost;
        this.minimumOrder = minimumOrder;
        this.productUnit = productUnit;
        this.productMeasurement = productMeasurement;
        this.defaultPhoto = defaultPhoto;
        this.category = category;
        this.subCategory = subCategory;
        this.ratingDetails = ratingDetails;
        this.likeCount = likeCount;
        this.imageCount = imageCount;
        this.favouriteCount = favouriteCount;
        this.touchCount = touchCount;
        this.featuredDate = featuredDate;
        this.commentHeaderCount = commentHeaderCount;
        this.originalPrice = originalPrice;
        this.unitPrice = unitPrice;
        this.discountAmount = discountAmount;
        this.currencyShortForm = currencyShortForm;
        this.currencySymbol = currencySymbol;
        this.discountPercent = discountPercent;
        this.discountValue = discountValue;
        this.isLiked = isLiked;
        this.isFavourited = isFavourited;
        this.overallRating = overallRating;
        this.transStatus = transStatus;
    }
}
