package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Rating {
    @SerializedName("id")
    @NonNull
    @PrimaryKey
    public final String id;

    @SerializedName("shop_id")
    public final String shopId;

    @SerializedName("user_id")
    public final String userId;

    @SerializedName("product_id")
    public final String productId;

    @SerializedName("rating")
    public final String rating;

    @SerializedName("title")
    public final String title;

    @SerializedName("description")
    public final String description;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("added_date_str")
    public final String addedDateStr;

    @SerializedName("user")
    @Embedded(prefix = "user")
    public final User user;

    public Rating(@NonNull String id, String shopId, String userId, String productId, String rating, String title, String description, String addedDate, String addedDateStr, User user) {
        this.id = id;
        this.shopId = shopId;
        this.userId = userId;
        this.productId = productId;
        this.rating = rating;
        this.title = title;
        this.description = description;
        this.addedDate = addedDate;
        this.addedDateStr = addedDateStr;
        this.user = user;
    }
}
