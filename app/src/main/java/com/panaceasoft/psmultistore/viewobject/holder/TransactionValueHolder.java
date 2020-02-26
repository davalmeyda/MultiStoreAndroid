package com.panaceasoft.psmultistore.viewobject.holder;

import com.panaceasoft.psmultistore.viewobject.BasketProductListToServerContainer;

public class TransactionValueHolder {

    public String currencySymbol = "",
            shippingMethodName = "",
            selectedShippingId = "";
    public float total = 0,
            discount = 0,
            coupon_discount = 0,
            sub_total = 0,
            tax = 0,
            shipping_cost = 0,
            shipping_tax = 0,
            final_total = 0;
    public int total_item_count = 0;
    public String couponDiscountText = "";
    public BasketProductListToServerContainer basketProductListToServerContainer;

    public TransactionValueHolder() {
        this.basketProductListToServerContainer = new BasketProductListToServerContainer();
    }

    public void resetValues()
    {
        currencySymbol = "";
        shippingMethodName = "";
        total = 0;
        discount = 0;
        tax = 0;
        shipping_tax = 0;
        final_total = 0;
        total_item_count = 0;
    }
}
