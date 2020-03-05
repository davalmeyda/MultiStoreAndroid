package com.panaceasoft.psmultistore.viewmodel.category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryDavid {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("added_date")
        @Expose
        private String addedDate;
        @SerializedName("updated_date")
        @Expose
        private String updatedDate;
        @SerializedName("added_user_id")
        @Expose
        private String addedUserId;
        @SerializedName("shop_id")
        @Expose
        private String shopId;
        @SerializedName("updated_user_id")
        @Expose
        private String updatedUserId;
        @SerializedName("updated_flag")
        @Expose
        private Integer updatedFlag;
        @SerializedName("touch_count")
        @Expose
        private Integer touchCount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getAddedDate() {
            return addedDate;
        }

        public void setAddedDate(String addedDate) {
            this.addedDate = addedDate;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

        public String getAddedUserId() {
            return addedUserId;
        }

        public void setAddedUserId(String addedUserId) {
            this.addedUserId = addedUserId;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getUpdatedUserId() {
            return updatedUserId;
        }

        public void setUpdatedUserId(String updatedUserId) {
            this.updatedUserId = updatedUserId;
        }

        public Integer getUpdatedFlag() {
            return updatedFlag;
        }

        public void setUpdatedFlag(Integer updatedFlag) {
            this.updatedFlag = updatedFlag;
        }

        public Integer getTouchCount() {
            return touchCount;
        }

        public void setTouchCount(Integer touchCount) {
            this.touchCount = touchCount;
        }


}
