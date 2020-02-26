package com.panaceasoft.psmultistore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;

@Entity(primaryKeys = "id")
public class Comment {

    @SerializedName("id")
    @NonNull
    public final String id;

    @SerializedName("shop_id")
    public final String shopId;

    @SerializedName("product_id")
    public final String productId;

    @SerializedName("user_id")
    public final String userId;

    @SerializedName("header_comment")
    public final String headerComment;

    @SerializedName("status")
    public final String status;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("updated_date")
    public final String updatedDate;

    @SerializedName("comment_reply_count")
    public final String commentReplyCount;

    @SerializedName("added_date_str")
    public final String addedDateStr;

    @Embedded(prefix = "user_")
    @SerializedName("user")
    public final User user;

    public Comment(@NonNull String id, String shopId, String productId, String userId, String headerComment, String status, String addedDate, String updatedDate, String commentReplyCount, String addedDateStr, User user) {
        this.id = id;
        this.shopId = shopId;
        this.productId = productId;
        this.userId = userId;
        this.headerComment = headerComment;
        this.status = status;
        this.addedDate = addedDate;
        this.updatedDate = updatedDate;
        this.commentReplyCount = commentReplyCount;
        this.addedDateStr = addedDateStr;
        this.user = user;
    }
}
