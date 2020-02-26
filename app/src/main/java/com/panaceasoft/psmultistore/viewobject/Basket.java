package com.panaceasoft.psmultistore.viewobject;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Basket {

    @PrimaryKey(autoGenerate = true)
    public int id = 0;

    public final String productId;

    public int count;

    public String selectedAttributes;

    public String selectedColorId;

    public String selectedColorValue;

    public String selectedAttributeTotalPrice;

    public String selectedAttributesPrice;

    public float basketPrice;

    public float basketOriginalPrice;

    public String shopId;

    @Ignore
    public Product product;

    public Basket(String productId, int count, String selectedAttributes, String selectedColorId, String selectedColorValue, String selectedAttributeTotalPrice, float basketPrice, float basketOriginalPrice, String shopId, String selectedAttributesPrice) {
        this.productId = productId;
        this.count = count;
        this.selectedAttributes = selectedAttributes;
        this.selectedColorId = selectedColorId;
        this.selectedColorValue = selectedColorValue;
        this.selectedAttributeTotalPrice = selectedAttributeTotalPrice;
        this.basketPrice = basketPrice;
        this.basketOriginalPrice = basketOriginalPrice;
        this.shopId = shopId;
        this.selectedAttributesPrice = selectedAttributesPrice;
    }
}
